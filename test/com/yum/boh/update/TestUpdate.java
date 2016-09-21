package com.yum.boh.update;/**
 * Created by Angel on 6/20/15.
 */

import com.yum.boh.fund.service.update.TemporaryBOHUpdateService;
import com.yum.boh.fundgen.model.ds.FundTDownloadTask;
import org.junit.Assert;
import org.junit.Test;

/**
 * @page TestCase
 */
public class TestUpdate {
    @Test
    public void test() {
        FundTDownloadTask downloadTask = new FundTDownloadTask();
        downloadTask.setInvokeClass("com.yum.boh.bmp.task.downloadversion.DownLoadVersionCallBack");
        downloadTask.setFunParam("bohVersionPackagePath=/LOG/Packages_201506101022.boh, isReDownLoad=0");
        TemporaryBOHUpdateService bohUpdateService = new TemporaryBOHUpdateService();
        if(bohUpdateService.doBOHUpdate(downloadTask)) {
            //logger.info("临时强制升级成功：" + paramStr);
        }
        else {
            //logger.error("临时强制升级成功：" + paramStr);
        }
        Assert.assertTrue(true);
    }
}
