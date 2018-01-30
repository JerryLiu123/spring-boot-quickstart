package com.liu.springboot.quickstart.util;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 编程式事物实现
 * @author lgh
 *
 */
@Component
public class TransactionUtil {

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    public boolean transact(Consumer consumer) {  
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());  
        try {  
            consumer.accept(null);  
  
            transactionManager.commit(status);  
            return true;  
        } catch (Exception e) {  
            transactionManager.rollback(status);  
            e.printStackTrace();  
            return false;  
        }  
  
  
    }
}
