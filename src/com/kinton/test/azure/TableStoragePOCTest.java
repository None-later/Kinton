package com.kinton.test.azure;/**
 * Created by mgkj1 on 2016/10/19.
 */

import com.kinton.test.azure.com.kinton.test.azure.model.TableStoragePOCModel;
import com.yum.boh.core.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;
import com.microsoft.azure.storage.table.TableQuery.*;

import java.util.*;

public class TableStoragePOCTest {
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=tablepoc;" +
                    "AccountKey=WQV0e3EApFEHvz0POcH4aD6EB/OI2E2nLWOgswGE3CzYmLBHqTI+bW/a6RekrNauSIIxJqNRSkXkl5Vc9iVjpQ==;" +
                    "EndpointSuffix=core.chinacloudapi.cn";
    public static final int MAX_BATCH_INSERT_LIMIT = 100;
    public static final String PARTITION_KEY = "PartitionKey";
    public static final String ROW_KEY = "RowKey";
    public static final String TIMESTAMP = "Timestamp";
    //DefaultEndpointsProtocol=https;AccountName=tablepoc;AccountKey=WQV0e3EApFEHvz0POcH4aD6EB/OI2E2nLWOgswGE3CzYmLBHqTI+bW/a6RekrNauSIIxJqNRSkXkl5Vc9iVjpQ==
    @Test
    public void test() {
        System.out.println("【Azure POC】开始创建表：people");
        createTable("people");
        System.out.println("【Azure POC】列出创建的表");
        listTable();
        //insert n models to table people
        int insertCount = 50000;
        List<TableStoragePOCModel> tableStoragePOCModelList = new ArrayList<>(insertCount);
        TableStoragePOCModel tableStoragePOCModel = null;
        Date currentDate = new Date();
        String storeCode = "SHA043";
//        for(int i=0;i<insertCount;i++) {
//            tableStoragePOCModel = new TableStoragePOCModel(UUID.randomUUID().toString(),20+i,currentDate,
//                    "testcomment" + i,UUID.randomUUID().toString(),storeCode);
//            tableStoragePOCModelList.add(tableStoragePOCModel);
//        }
//
//        System.out.println("start insert models:" + StringUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));
//        insertModels(tableStoragePOCModelList,"people");
//        System.out.println("end insert models:" + StringUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));

//        tableStoragePOCModelList.clear();
        for(int i=0;i<insertCount;i++) {
            tableStoragePOCModel = new TableStoragePOCModel(UUID.randomUUID().toString(),20+i,currentDate,
                    "testcomment" + i,UUID.randomUUID().toString(),storeCode);
            tableStoragePOCModelList.add(tableStoragePOCModel);
        }
        System.out.println("start batchinsert models:" + StringUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));
        insertBatchModels(tableStoragePOCModelList,"people");
        System.out.println("end batchinsert models:" + StringUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));



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

    private boolean insertModels(List<TableStoragePOCModel> insertList, String tableName) {
        boolean retValue = false;
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount =
                    CloudStorageAccount.parse(storageConnectionString);

            // Create the table client.
            CloudTableClient tableClient = storageAccount.createCloudTableClient();

            // Create a cloud table object for the table.
            CloudTable cloudTable = tableClient.getTableReference(tableName);

            // Create an operation to add the new customer to the people table.
            TableOperation insertModels = null;

            for(TableStoragePOCModel tableStoragePOCModel:insertList) {
                insertModels = TableOperation.insertOrReplace(tableStoragePOCModel);
                // Submit the operation to the table service.
                cloudTable.execute(insertModels);
            }
            retValue = true;
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * batch insert models
     * @param insertList insert moedels list
     * @param tableName the target table name
     * @return insert sucessfully or not
     */
    private boolean insertBatchModels(List<TableStoragePOCModel> insertList, String tableName) {
        boolean retValue = false;
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount =
                    CloudStorageAccount.parse(storageConnectionString);

            // Create the table client.
            CloudTableClient tableClient = storageAccount.createCloudTableClient();

            // Define a batch operation.
            TableBatchOperation batchOperation = new TableBatchOperation();

            // Create a cloud table object for the table.
            CloudTable cloudTable = tableClient.getTableReference(tableName);

            //loop the list 4 insert
            int index = 0;
            for(TableStoragePOCModel tableStoragePOCModel:insertList) {
                batchOperation.insertOrReplace(tableStoragePOCModel);
                index++;
                if(index>=MAX_BATCH_INSERT_LIMIT) {
                    cloudTable.execute(batchOperation);
                    index = 0;
                    batchOperation.clear();
                }
            }
            if(index>0) {
                cloudTable.execute(batchOperation);
            }
            // Execute the batch of operations on the "people" table.
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
        return retValue;
    }


}