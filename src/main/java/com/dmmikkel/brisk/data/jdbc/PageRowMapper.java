package com.dmmikkel.brisk.data.jdbc;

import com.dmmikkel.brisk.data.model.Page;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PageRowMapper
        implements RowMapper<Page>
{
    @Override
    public Page mapRow(ResultSet rs, int i)
            throws SQLException
    {
        Page page = new Page();

        page.setSiteKey(rs.getString("site_key"));
        page.setName(rs.getString("name"));
        page.setKey(rs.getString("key"));
        page.setMasterTemplate(rs.getString("master_template"));
        page.setTemplate(rs.getString("template"));
        page.setContentQueryMethod(rs.getInt("content_query_method"));
        page.setContentKey(rs.getString("content_key"));
        page.setContentType(rs.getString("content_type"));
        page.setOrderBy(rs.getString("content_order_by"));
        page.setOrderDirection(rs.getString("content_order_direction"));
        page.setCount(rs.getInt("content_count"));

        return page;
    }
}
