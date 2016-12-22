package com.kinton.test.azure.com.kinton.test.azure.model;/**
 * Created by mgkj1 on 12/21/2016.
 */

import com.kinton.test.azure.TableStoragePOCTest;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.yum.boh.core.util.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TableStoragePOCTestQuery {
    @Test
    public void test() {
        System.out.println("start query models in partition:" + StringUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));
        Map<String,Object> queryParam = new HashMap<>();
        queryParam.put("StoreCode","SHA037");
        Iterable<TableStoragePOCModel> tableStoragePOCModelIterable = queryTableStorageListInPartition(queryParam,"people");
        System.out.println("query end models in partition:" + StringUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));
        int i = 0;
        if(tableStoragePOCModelIterable!=null) {//测试
            for(TableStoragePOCModel tableStoragePOCModel1 : tableStoragePOCModelIterable) {
//                System.out.println("query result:" + tableStoragePOCModel1.getOrderID() + ",storeCode:" + tableStoragePOCModel1.getStoreCode() + ",customerID"
//                        + tableStoragePOCModel1.getCustomerID() + ",orderDate:" + tableStoragePOCModel1.getOrderDate());
                i++;
            }
        }
        System.out.println("end query models in partition:" + StringUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss.SSS") + ",record count:" + i);

        System.out.println("start retrieveTableStorageModelInPartition model:" + StringUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss.SSS") + ",record count:" + i);
        queryParam.put("OrderID","022ba054-955f-467a-9465-3da660e41663");
        TableStoragePOCModel retrieveModel = retrieveTableStorageModelInPartition(queryParam,"people");
        System.out.println("end retrieveTableStorageModelInPartition model:" + StringUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss.SSS") + ",record count:" + i);
        Assert.assertTrue(true);
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
                    CloudStorageAccount.parse(TableStoragePOCTest.storageConnectionString);

            // Create the table client.
            CloudTableClient tableClient = storageAccount.createCloudTableClient();

            // Create a cloud table object for the table.
            CloudTable cloudTable = tableClient.getTableReference(tableName);

            // Create a filter condition where the partition key is "Smith".
            String partitionFilter = TableQuery.generateFilterCondition(
                    TableStoragePOCTest.PARTITION_KEY,//storeCode
                    TableQuery.QueryComparisons.EQUAL,
                    queryParam.get("StoreCode").toString());

            String rowFilter = TableQuery.generateFilterCondition(
                    "RowKey",
                    TableQuery.QueryComparisons.LESS_THAN,
                    "T");
            String combinedFilter = TableQuery.combineFilters(partitionFilter,
                    TableQuery.Operators.AND, rowFilter);
            // Specify a partition query, using "SHA037" as the partition key filter.
            TableQuery<TableStoragePOCModel> partitionQuery =
                    TableQuery.from(TableStoragePOCModel.class)
                            .where(combinedFilter);
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

    private TableStoragePOCModel retrieveTableStorageModelInPartition(Map<String,Object> queryParam,String tableName) {
        TableStoragePOCModel retValue = null;
        try
        {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount =
                    CloudStorageAccount.parse(TableStoragePOCTest.storageConnectionString);

            // Create the table client.
            CloudTableClient tableClient = storageAccount.createCloudTableClient();

            // Create a cloud table object for the table.
            CloudTable cloudTable = tableClient.getTableReference(tableName);

            // Retrieve the entity with partition key of "Smith" and row key of "Jeff"
            TableOperation retrieveModel =
                    TableOperation.retrieve(queryParam.get("StoreCode").toString(),
                            queryParam.get("OrderID").toString(), TableStoragePOCModel.class);

            // Submit the operation to the table service and get the specific entity.
            retValue =
                    cloudTable.execute(retrieveModel).getResultAsType();

            // Output the entity.

        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
        return  retValue;
    }
}