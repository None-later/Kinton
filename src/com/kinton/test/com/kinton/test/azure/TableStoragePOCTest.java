package com.kinton.test.com.kinton.test.azure;/**
 * Created by mgkj1 on 2016/10/19.
 */

import org.junit.Assert;
import org.junit.Test;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;
import com.microsoft.azure.storage.table.TableQuery.*;

public class TableStoragePOCTest {
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=tablepoc;" +
                    "AccountKey=WQV0e3EApFEHvz0POcH4aD6EB/OI2E2nLWOgswGE3CzYmLBHqTI+bW/a6RekrNauSIIxJqNRSkXkl5Vc9iVjpQ==;" +
                    "EndpointSuffix=core.chinacloudapi.cn";
    //DefaultEndpointsProtocol=https;AccountName=tablepoc;AccountKey=WQV0e3EApFEHvz0POcH4aD6EB/OI2E2nLWOgswGE3CzYmLBHqTI+bW/a6RekrNauSIIxJqNRSkXkl5Vc9iVjpQ==
    @Test
    public void test() {
        System.out.println("【Azure POC】开始创建表：people");
        createTable("people");
        System.out.println("【Azure POC】列出创建的表");
        listTable();
        Assert.assertTrue(true);
    }

    private void createTable(String tableName) {
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount =
                    CloudStorageAccount.parse(storageConnectionString);

            // Create the table client.
            CloudTableClient tableClient = storageAccount.createCloudTableClient();

            // Create the table if it doesn't exist.
            CloudTable cloudTable = tableClient.getTableReference(tableName);
            cloudTable.createIfNotExists();
            System.out.println("【Azure POC】建表成功：" + tableName);

        }
        catch (Exception e)
        {
            // Output the stack trace.
            System.out.println("【Azure POC】建表错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listTable() {
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount =
                    CloudStorageAccount.parse(storageConnectionString);

            // Create the table client.
            CloudTableClient tableClient = storageAccount.createCloudTableClient();

            // Loop through the collection of table names.
            for (String table : tableClient.listTables())
            {
                // Output each table name.
                System.out.println("【Azure POC】表列表：" + table);
            }
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
    }
}