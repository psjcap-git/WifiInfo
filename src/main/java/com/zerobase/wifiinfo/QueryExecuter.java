package com.zerobase.wifiinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.*;

public class QueryExecuter {
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class Response extends ResponseBase {
        private int executeCount;

        public Response(boolean isError, String errorMessage) {
            super(isError, errorMessage);
            this.executeCount = 0;
        }

        public Response(boolean isError, String errorMessage, int executeCount) {
            super(isError, errorMessage);
            this.executeCount = executeCount;
        }
    }

    @FunctionalInterface
    public static interface QueryExecuterPredicate {
        public void execute(ResultSet rs) throws SQLException;
    }

    private static final String JDBCNAME = "org.sqlite.JDBC";
    private static final String DBPATH = "jdbc:sqlite:C:\\Work\\HomeWork\\WifiInfo\\WifiInfoDB.db";

    private Connection connection;
    private PreparedStatement preparedStatement;

    private Connection getConnection() {
        if(connection != null) {
            return connection;
        }

        try {
            Class.forName(JDBCNAME);
            connection = DriverManager.getConnection(DBPATH);
        } catch(Exception ex) {
            connection = null;
        }
        return connection;
    }

    public Response prepare(String query) {
        Response response = null;

        try {
            preparedStatement = this.getConnection().prepareStatement(query);
            response = new Response(false, "SUCCESS");
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }

        return response;
    }

    public void release() {
        try {
            if(preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }

            if(connection != null) {
                connection.close();
                connection = null;
            }
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }

    public Response startTransaction() {
        Response response = null;
        try {
            this.getConnection().setAutoCommit(false);
            response = new Response(false, "SUCCESS");
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }
        return response;
    }

    public Response commit() {
        Response response = null;
        try {
            this.getConnection().commit();
            response = new Response(false, "SUCCESS");
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }
        return response;
    }

    public Response rollback() {
        Response response = null;
        try {
            this.getConnection().rollback();
            response = new Response(false, "SUCCESS");
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }
        return response;
    }

    public Response executeUpdate() {
        if(preparedStatement == null) {
            return new Response(true, "DB is not prepared");
        }

        Response response;
        try {
            response = new Response(false, "SUCCESS", preparedStatement.executeUpdate());
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }

        return response;
    }

    public Response executeQuery(QueryExecuterPredicate predicate) {
        if(preparedStatement == null) {
            return new Response(true, "DB is not prepared");
        }

        Response response = null;
        try {
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                predicate.execute(rs);
            }
            response = new Response(false, "SUCCESS", rs.getFetchSize());
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }

        return response;
    }

    public Response setBoolean(int index, boolean value) {
        if(preparedStatement == null) {
            return new Response(true, "DB is not prepared");
        }

        Response response = null;
        try {
            preparedStatement.setBoolean(index, value);
            response = new Response(false, "SUCCESS", 0);
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }
        return response;
    }

    // Add if needed
    public Response setInt(int index, int value) {
        if(preparedStatement == null) {
            return new Response(true, "DB is not prepared");
        }

        Response response = null;
        try {
            preparedStatement.setInt(index, value);
            response = new Response(false, "SUCCESS", 0);
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }
        return response;
    }

    public Response setFloat(int index, float value) {
        if(preparedStatement == null) {
            return new Response(true, "DB is not prepared");
        }

        Response response = null;
        try {
            preparedStatement.setFloat(index, value);
            response = new Response(false, "SUCCESS", 0);
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }
        return response;
    }

    public Response setString(int index, String value) {
        if(preparedStatement == null) {
            return new Response(true, "DB is not prepared");
        }

        Response response = null;
        try {
            preparedStatement.setString(index, value);
            response = new Response(false, "SUCCESS", 0);
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }
        return response;
    }

    public Response setObject(int index, Object value) {
        if(preparedStatement == null) {
            return new Response(true, "DB is not prepared");
        }

        Response response = null;
        try {
            preparedStatement.setObject(index, value);
            response = new Response(false, "SUCCESS", 0);
        } catch(SQLException se) {
            response = new Response(true, se.getMessage());
        }
        return response;
    }
}
