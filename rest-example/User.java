import org.apache.camel.BindToRegistry;
import java.util.*;
import org.apache.camel.Exchange;
import com.google.gson.Gson;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("userBean")
@BindToRegistry("userBean")
@ApplicationScoped
public class User{

    public HashMap<String, String> findUserById(Exchange exchange){
        System.out.println("Inside the method findUserById");
        String path = exchange.getIn().getHeader(Exchange.HTTP_PATH).toString();
        System.out.println(path);
        String[] uri = path.split("/");
        String id = uri[uri.length-1];
        HashMap<String, String> paramValues = new HashMap<String, String>();
        paramValues.put("id", id);
        return paramValues;
    }
    
    public HashMap<String, String> getAllUsers(Exchange exchange){
        System.out.println("Inside getAllUsers");
        HashMap<String, String> map = new HashMap<String, String>();
        return map;
    }

    public void returnResponseForCreate(Exchange exchange){
        System.out.println("Inside returnResponseForCreate");
        exchange.getOut().setBody(exchange.getIn().getBody());
        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, "201");
    }

    public HashMap<String, String> updateUserById(Exchange exchange){
        System.out.println("Inside updateUserById");
        HashMap<String, String> sqlQueryParams = findUserById(exchange);
        String requestBody = exchange.getIn().getBody(String.class);
        Gson gson = new Gson();
        Map<String, String> requestMap = gson.fromJson(requestBody, Map.class);
        sqlQueryParams.put("name", requestMap.get("name").toString());
        return sqlQueryParams;
    }

    public HashMap<String, String> patchUserById(Exchange exchange){
        System.out.println("Inside patch");
        HashMap<String, String> allParams = new HashMap<String, String>();
        String query = exchange.getIn().getHeader(Exchange.HTTP_QUERY).toString();
        System.out.println(query);
        String[] queryParams = query.split("&");
        for(String param:queryParams){
            if(param.contains("id")){
                String paramValue = param.split("=")[1];
                allParams.put("id", paramValue);
            }
        }
        String headerParam = exchange.getIn().getHeader("name").toString();
        System.out.println(headerParam);
        allParams.put("name", headerParam);
        return allParams;
    }

}