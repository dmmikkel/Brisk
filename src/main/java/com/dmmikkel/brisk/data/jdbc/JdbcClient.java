package com.dmmikkel.brisk.data.jdbc;

import com.dmmikkel.brisk.data.Client;
import com.dmmikkel.brisk.data.exception.ClientException;
import com.dmmikkel.brisk.data.exception.ConnectionException;
import com.dmmikkel.brisk.data.exception.NotFoundException;
import com.dmmikkel.brisk.data.model.ContentCollection;
import com.dmmikkel.brisk.data.model.Image;
import com.dmmikkel.brisk.data.model.Page;
import com.dmmikkel.brisk.data.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class JdbcClient
        implements Client
{
    private static RowMapper<Page> pageRowMapper = new PageRowMapper();

    private JdbcTemplate jdbcTemplate;

    public JdbcClient()
    {
        try
        {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/cms");
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        catch (Exception e)
        {
            throw new ClientException("Unable to initiate JdbcTemplate", e);
        }

    }

    @Override
    public Site getSiteFromDomain(String domain)
            throws ClientException
    {
        return null;
    }

    @Override
    public Map<String, Site> getSites()
            throws ClientException
    {
        return null;
    }

    @Override
    public Site getSite(String siteKey)
            throws ClientException
    {
        return null;
    }

    @Override
    public Map<String, String> getDomains()
            throws ClientException
    {
        return null;
    }

    @Override
    public List<Page> getPages(String siteKey)
    {
        return jdbcTemplate.query("SELECT * FROM pages WHERE site_key = ?", new Object[]{siteKey}, pageRowMapper);
    }

    @Override
    public Page getPage(String siteKey, String pageKey)
            throws ClientException
    {
        return jdbcTemplate.queryForObject("SELECT * FROM pages WHERE site_key = ? AND key = ?", new Object[]{siteKey, pageKey}, pageRowMapper);
    }

    @Override
    public void savePage(Page page)
            throws ClientException
    {
        int count = jdbcTemplate.queryForObject("SELECT count(*) FROM pages WHERE site_key = ? AND key = ?", new Object[] { page.getSiteKey(), page.getKey() }, Integer.class);
        if (count == 0)
        {
            jdbcTemplate.update("INSERT INTO pages (site_key, key, name, master_template, template, content_query_method, content_key, content_type, content_order_by, content_order_direction, content_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    page.getSiteKey(),
                    page.getKey(),
                    page.getName(),
                    page.getMasterTemplate(),
                    page.getTemplate(),
                    page.getContentQueryMethod(),
                    page.getContentKey(),
                    page.getContentType(),
                    page.getOrderBy(),
                    page.getOrderDirection(),
                    page.getCount()
            );
        } else {
            jdbcTemplate.update("UPDATE pages SET name = ?, master_template = ?, template = ?, content_query_method = ?, content_key = ?, content_type = ?, content_order_by = ?, content_order_direction = ?, content_count = ? WHERE site_key = ? AND key = ?",
                    page.getName(),
                    page.getMasterTemplate(),
                    page.getTemplate(),
                    page.getContentQueryMethod(),
                    page.getContentKey(),
                    page.getContentType(),
                    page.getOrderBy(),
                    page.getOrderDirection(),
                    page.getCount(),
                    page.getSiteKey(),
                    page.getKey()
            );
        }
    }

    @Override
    public Image getImage(String imageKey)
            throws ClientException
    {
        return null;
    }

    @Override
    public ContentCollection getContentByKey(String siteKey, String key)
            throws ConnectionException, NotFoundException
    {
        return null;
    }

    @Override
    public ContentCollection getContentByType(String siteKey, String contentType, String orderBy, String orderDirection, int count, int page)
            throws ConnectionException, NotFoundException
    {
        return null;
    }
}
