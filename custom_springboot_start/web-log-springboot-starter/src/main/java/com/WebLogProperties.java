package com;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
 
@ConfigurationProperties("weblog")
@Data
public class WebLogProperties{
 
    public Boolean enabled;  //Boolean封装类，默认为null

}