package com.dmmikkel.cms.data;

import com.dmmikkel.cms.data.exception.ClientException;
import com.dmmikkel.cms.data.exception.ConnectionException;
import com.dmmikkel.cms.data.exception.NotFoundException;
import com.dmmikkel.cms.data.model.*;
import com.google.gson.Gson;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostgresqlClient
        implements Client
{

    private Connection getConnection()
            throws ConnectionException
    {
        try
        {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/cms");
            return dataSource.getConnection();
        }
        catch (NamingException e)
        {
            throw new ConnectionException("Failed lookup for jdbc/cms..", e);
        }
        catch (SQLException e)
        {
            throw new ConnectionException("Could not find datasource.", e);
        }

    }

    @Override
    public Site getSiteFromDomain(String domain)
            throws NotFoundException, ConnectionException
    {
        Connection connection = getConnection();
        try
        {
            final String getSiteFromDomainQuery = "SELECT sites.key, sites.name FROM sites INNER JOIN domains ON domains.site_key = sites.key WHERE domains.domain = ?";
            PreparedStatement statement = connection.prepareStatement(getSiteFromDomainQuery);
            statement.setString(1, domain);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next())
            {
                throw new NotFoundException("Could not locate site with domain = " + domain);
            }

            Site site = new Site();
            site.key = resultSet.getString("key");
            site.name = resultSet.getString("name");

            connection.close();
            return site;
        }
        catch (SQLException e)
        {
            throw new ConnectionException("Could not prepare SQL statement.", e);
        }
    }

    @Override
    public Map<String, String> getDomains()
            throws NotFoundException, ConnectionException
    {
        Connection connection = getConnection();
        try
        {
            final String query = "SELECT * FROM domains";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet resultSet = statement.getResultSet();
            HashMap<String, String> domains = new HashMap<>();

            while (resultSet.next())
            {
                domains.put(resultSet.getString("domain"), resultSet.getString("site_key"));
            }

            connection.close();
            return domains;
        }
        catch (SQLException e)
        {
            throw new ConnectionException("Could not prepare SQL statement.", e);
        }
    }

    public Map<String, Site> getSites()
            throws NotFoundException, ConnectionException
    {
        Connection connection = getConnection();
        try
        {
            final String query = "SELECT * FROM sites";
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet resultSet = statement.getResultSet();
            HashMap<String, Site> sites = new HashMap<>();

            while (resultSet.next())
            {
                Site site = new Site();
                site.key = resultSet.getString("key");
                site.name = resultSet.getString("name");
                sites.put(resultSet.getString("key"), site);
            }

            connection.close();
            return sites;
        }
        catch (SQLException e)
        {
            throw new ConnectionException("Could not prepare SQL statement.", e);
        }
    }

    @Override
    public Site getSite(String siteKey)
            throws NotFoundException, ConnectionException
    {
        Connection connection = getConnection();
        try
        {
            final String getSiteFromIdQuery = "SELECT * FROM sites WHERE sites.key = ?";
            PreparedStatement statement = connection.prepareStatement(getSiteFromIdQuery);
            statement.setString(1, siteKey);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                throw new NotFoundException("Site not found.");
            Site site = new Site();
            site.key = resultSet.getString("key");
            site.name = resultSet.getString("name");
            return site;
        }
        catch (SQLException e)
        {
            throw new ConnectionException("Could not prepare SQL statement.", e);
        }
        finally
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Page getPage(String siteKey, String pageKey)
    {
        Connection connection = getConnection();
        try
        {
            final String getSiteFromIdQuery = "SELECT * FROM pages WHERE site_key = ? AND key = ?";

            PreparedStatement statement = connection.prepareStatement(getSiteFromIdQuery);
            statement.setString(1, siteKey);
            statement.setString(2, pageKey);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next())
                throw new NotFoundException("Page \"" + pageKey + "\" not found in database.");

            Page page = new Page();

            page.key = resultSet.getString("key");
            page.name = resultSet.getString("name");
            page.masterTemplate = resultSet.getString("master_template");
            page.template = resultSet.getString("template");
            page.contentQueryMethod = resultSet.getInt("content_query_method");
            page.contentKey = resultSet.getString("content_key");
            page.contentType = resultSet.getString("content_type");
            page.orderBy = resultSet.getString("content_order_by");
            page.orderDirection = resultSet.getString("content_order_direction");
            page.count = resultSet.getInt("content_count");

            return page;
        }
        catch (SQLException e)
        {
            throw new ConnectionException("Could not prepare SQL statement.", e);
        }
        finally
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Image getImage(String imageKey)
            throws ClientException
    {
        Connection connection = getConnection();
        try
        {
            final String getSiteFromIdQuery = "SELECT * FROM images WHERE key = ?";

            PreparedStatement statement = connection.prepareStatement(getSiteFromIdQuery);
            statement.setString(1, imageKey);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next())
            {
                throw new NotFoundException("Image not found: " + imageKey);
            }

            Image image = new Image();
            image.key = resultSet.getString("key");
            image.siteKey = resultSet.getString("site_key");
            image.filename = resultSet.getString("filename");
            image.name = resultSet.getString("name");

            connection.close();
            return image;
        }
        catch (SQLException e)
        {
            throw new ConnectionException("Could not prepare SQL statement.", e);
        }
    }

    @Override
    public ContentCollection getContentByKey(String siteKey, String key)
    {
        Connection connection = getConnection();
        try
        {
            Gson gson = new Gson();
            final String getSiteFromIdQuery = "SELECT * FROM contents WHERE site_key = ? AND key = ?";

            PreparedStatement statement = connection.prepareStatement(getSiteFromIdQuery);
            statement.setString(1, siteKey);
            statement.setString(2, key);
            ResultSet resultSet = statement.executeQuery();

            ContentCollection contents = new ContentCollection();
            contents.contents = new ArrayList<>();
            contents.page = 1;
            contents.totalPages = 1;
            contents.count = 0;

            while (resultSet.next())
            {
                contents.count++;
                Content content = new Content();
                content.key = resultSet.getString("key");
                content.contentdata = gson.fromJson(resultSet.getString("contentdata"), Map.class);
                content.publishfrom = resultSet.getDate("publishfrom");
                content.publishto = resultSet.getDate("publishto");
                content.created = resultSet.getDate("created");
                content.updated = resultSet.getDate("updated");
                content.order = resultSet.getInt("customorder");
                contents.contents.add(content);
            }
            return contents;
        }
        catch (SQLException e)
        {
            throw new ConnectionException("Could not prepare SQL statement.", e);
        }
        finally
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ContentCollection getContentByType(String siteKey, String contentType, String orderBy, String orderDirection, int count, int page)
            throws ConnectionException, NotFoundException
    {
        try
        {
            if (!orderBy.equals("publishfrom") && !orderBy.equals("created") && !orderBy.equals("updated") && !orderBy.equals("customorder"))
                throw new IllegalArgumentException("Order by not valid: " + orderBy);
            if (!orderDirection.equals("desc") && !orderDirection.equals("asc"))
                throw new IllegalArgumentException("Order direction must be 'asc' or 'desc'");

            Connection connection = getConnection();
            Gson gson = new Gson();
            String getSiteFromContentTypeQuery = String.format("SELECT * FROM contents WHERE site_key = ? AND contenttype = ? ORDER BY %s %s", orderBy, orderDirection);

            PreparedStatement statement = connection.prepareStatement(getSiteFromContentTypeQuery);
            statement.setString(1, siteKey);
            statement.setString(2, contentType);
            ResultSet resultSet = statement.executeQuery();

            ContentCollection contents = new ContentCollection();
            contents.contents = new ArrayList<>();
            contents.page = page;
            contents.count = count;
            contents.totalPages = 1; // TODO set number of pages correctly

            Content content;
            while (resultSet.next())
            {
                content = new Content();
                content.key = resultSet.getString("key");
                content.contentdata = gson.fromJson(resultSet.getString("contentdata"), Map.class);
                content.publishfrom = resultSet.getDate("publishfrom");
                content.publishto = resultSet.getDate("publishto");
                content.created = resultSet.getDate("created");
                content.updated = resultSet.getDate("updated");
                content.order = resultSet.getInt("customorder");
                contents.contents.add(content);
            }

            connection.close();
            return contents;
        }
        catch (SQLException e)
        {
            throw new ConnectionException("Could not prepare SQL statement.", e);
        }
    }
}
