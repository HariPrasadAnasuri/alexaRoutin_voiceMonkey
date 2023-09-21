package com.bvirtuoso.hari.service

import com.bvirtuoso.hari.api.ApiInvoker
import com.bvirtuoso.hari.repository.DishInfoRepository
import com.bvirtuoso.hari.repository.HealthInfoRepository
import com.bvirtuoso.hari.repository.TvOnOffRepository
import com.sun.jna.platform.win32.W32Service
import com.sun.jna.platform.win32.W32ServiceManager
import com.sun.jna.platform.win32.Win32Exception
import com.sun.jna.platform.win32.Winsvc
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class NgrokService {

    private final static Log log = LogFactory.getLog(NgrokService.class)

    private final ApiInvoker apiInvoker

    NgrokService(ApiInvoker apiInvoker){
        log.debug("Scheduler initialized")
        this.apiInvoker = apiInvoker
    }

    String getNgrokUrl(){
        def ngrokResponse = apiInvoker.getNgrokUrl("https://api.ngrok.com/tunnels");
        String ngrokUrl
        if(ngrokResponse.tunnels && ngrokResponse.tunnels[0] && ngrokResponse.tunnels[0].public_url){
            ngrokUrl = ngrokResponse.tunnels[0].public_url
            log.debug("Ngrok URL: ${ngrokUrl}")
            return ngrokUrl;
        }else{
            restartNgrokService();
            ngrokResponse = apiInvoker.getNgrokUrl("https://api.ngrok.com/tunnels");
            if(ngrokResponse.tunnels && ngrokResponse.tunnels[0] && ngrokResponse.tunnels[0].public_url) {
                ngrokUrl = ngrokResponse.tunnels[0].public_url
                log.debug("Ngrok URL: ${ngrokUrl}")
            }
            log.debug("Got the ngrok URL afer restarting the service: ${ngrokUrl}")
            return ngrokUrl
        }
    }
    void restartNgrokService(){
        log.debug("Announcing drink water")
        String serviceName = "ngrok"
        try {
            try
            {
                W32ServiceManager serviceManager = new W32ServiceManager()
                serviceManager.open(Winsvc.SC_MANAGER_ALL_ACCESS)
                W32Service service = serviceManager.openService(serviceName, Winsvc.SC_MANAGER_ALL_ACCESS)
                service.stopService()
                Thread.sleep(3000)
                service.startService()
                service.close()
                Thread.sleep(3000)
                /*def ngrokResponse = apiInvoker.getNgrokUrl("https://api.ngrok.com/tunnels");
                restApiEndpoint.ngrokUrl = ngrokResponse.tunnels[0].public_url
                log.debug("ngrok URL fetched: ${restApiEndpoint.ngrokUrl}")
                def responseFromLambda = apiInvoker.setNgrokUrlToLambdaFunction(
                        "https://haaiv4ssyv5urvwzicvhgqijyu0kjueo.lambda-url.us-east-1.on.aws/", restApiEndpoint.ngrokUrl)

*/
            } catch (Exception ex)
            {
                log.debug("Exception when dealing with service: ${ex.printStackTrace()}")
                ex.printStackTrace();
            }

        } catch (Win32Exception | InterruptedException e) {
            log.debug("Exception when dealing with service: ${e.printStackTrace()}")
        }
    }
}
