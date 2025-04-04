package com.beyond3.yyGang;

import java.sql.Connection;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConn {

    public static void main(String[] args) {
        // DB랑 프로젝트 연결 잘 됐는지 확인
        Connection conn = null;

        final String driver = "org.mariadb.jdbc.Driver";
        final String DB_IP = "localhost";
        final String DB_PORT = "3306";
        final String DB_NAME = "yygang_demo_db4";
        final String DB_URL =
                "jdbc:mariadb://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME;
        try {
            // MariaDB 드라이버 로드
            Class.forName(driver);

            // DB 연결
            conn = DriverManager.getConnection(DB_URL, "beyondT3", "0000");
            System.out.println("Database connected successfully!");

            // 데이터베이스 메타데이터 얻기
            DatabaseMetaData metaData = conn.getMetaData();

            // 테이블 목록 가져오기
            ResultSet tables = metaData.getTables(null, null, "%", new String[] {"TABLE"});

            System.out.println("Tables in the database:");
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println(tableName);
            }

        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found!");
            e.printStackTrace();
        } finally {
            // 연결 종료
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
