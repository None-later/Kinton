package com.kinton.test.azure;/**
 * Created by mgkj1 on 2016/10/19.
 */

import com.kinton.test.azure.com.kinton.test.azure.model.TableStoragePOCModel;
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
    private static final int MAX_BATCH_INSERT_LIMIT = 100;
    private static final String PARTITION_KEY = "PartitionKey";
    private static final String ROW_KEY = "RowKey";
    private static final String TIMESTAMP = "Timestamp";
    //DefaultEndpointsProtocol=https;AccountName=tablepoc;AccountKey=WQV0e3EApFEHvz0POcH4aD6EB/OI2E2nLWOgswGE3CzYmLBHqTI+bW/a6RekrNauSIIxJqNRSkXkl5Vc9iVjpQ==
    @Test
    public void test() {
        System.out.println("【Azure POC】开始创建表：people");
        createTable("people");
        System.out.println("【Azure POC】列出创建的表");
        listTable();
        //insert n models to table people
        int insertCount = 1000;
        List<TableStoragePOCModel> tableStoragePOCModelList = new ArrayList<>(insertCount);
        TableStoragePOCModel tableStoragePOCModel = null;
        Date currentDate = new Date();
        String storeCode = "SHA037";
        for(int i=0;i<insertCount;i++) {
            tableStoragePOCModel = new TableStoragePOCModel(UUID.randomUUID().toString(),Float.valueOf("20.0"),currentDate,
                    "testcomment",UUID.randomUUID().toString(),storeCode);
            tableStoragePOCModelList.add(tableStoragePOCModel);
        }
        System.out.print("start insert models:" + new Date().toString());
        insertModels(tableStoragePOCModelList,"people");
        System.out.print("end insert models:" + new Date().toString());

        System.out.print("start batchinsert models:" + new Date().toString());
        insertBatchModels(tableStoragePOCModelList,"people");
        System.out.print("end batchinsert models:" + new Date().toString());

        System.out.print("start query models in partition:" + new Date().toString());
        Map<String,Object> queryParam = new HashMap<>();
        queryParam.put("StoreCode","SHA037");
        Iterable<TableStoragePOCModel> tableStoragePOCModelIterable = queryTableStorageListInPartition(queryParam,"people");
        if(tableStoragePOCModelIterable!=null) {//测试
            for(TableStoragePOCModel tableStoragePOCModel1 : tableStoragePOCModelIterable) {
                System.out.print("query result:" + tableStoragePOCModel.getOrderID() + ",storeCode:" + tableStoragePOCModel.getStoreCode() + ",customerID"
                + tableStoragePOCModel.getCustomerID() + ",orderDate:" + tableStoragePOCModel.getOrderDate());
            }
        }
        System.out.print("end query models in partition:" + new Date().toString());

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

    /**
     *
     * @param queryParam
     * @param tableName
     * @return
     */
    private Iterable<TableStoragePOCModel> queryTableStorageListInPartition(Map<String,Object> queryParam,String tableName) {
        Iterable<TableStoragePOCModel> tableStoragePOCModelList = null;
        try
        {

            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount =
                    CloudStorageAccount.parse(storageConnectionString);

            // Create the table client.
            CloudTableClient tableClient = storageAccount.createCloudTableClient();

            // Create a cloud table object for the table.
            CloudTable cloudTable = tableClient.getTableReference(tableName);

            // Create a filter condition where the partition key is "Smith".
            String partitionFilter = TableQuery.generateFilterCondition(
                    PARTITION_KEY,//storeCode
                    QueryComparisons.EQUAL,
                    queryParam.get("StoreCode").toString());

            // Specify a partition query, using "SHA037" as the partition key filter.
            TableQuery<TableStoragePOCModel> partitionQuery =
                    TableQuery.from(TableStoragePOCModel.class)
                            .where(partitionFilter);
            tableStoragePOCModelList = cloudTable.execute(partitionQuery);
            // Loop through the results, displaying information about the entity.
//            for (TableStoragePOCModel entity : tableStoragePOCModelList) {
//                System.out.println(entity.getPartitionKey() +
//                        " " + entity.getRowKey() +
//                        "\t" + entity.getEmail() +
//                        "\t" + entity.getPhoneNumber());
//            }
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
        return tableStoragePOCModelList;
    }
}