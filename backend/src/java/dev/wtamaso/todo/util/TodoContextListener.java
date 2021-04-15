package dev.wtamaso.todo.util;

import org.hibernate.SessionFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class TodoContextListener implements ServletContextListener {
    public static ServletContext servletContext;
    public static SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        sessionFactory = SessionFactoryUtil.getSessionFactory();

        System.out.println("===>>> Server Started <<<===");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();
        }

        System.out.println("===>>> Server Stopped <<<===");
    }
}
