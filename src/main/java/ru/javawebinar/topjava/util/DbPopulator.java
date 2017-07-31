package ru.javawebinar.topjava.util;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;

public class DbPopulator extends ResourceDatabasePopulator {
    private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

    private final DataSource dataSource;

    public DbPopulator(DataSource dataSource, String... scriptLocation) {
        super(getResources(scriptLocation));
        this.dataSource = dataSource;
    }

    private static Resource[] getResources(String... scriptLocation) {
        List<Resource> resources = new LinkedList<>();
        for (String s : scriptLocation) {
            resources.add(RESOURCE_LOADER.getResource(s));
        }
        Resource[] resources1 = new Resource[resources.size()];
        return (resources.toArray(resources1));
    }

    public void execute() {
        execute(dataSource);
    }
}