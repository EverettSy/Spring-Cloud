package com.syrobin.cloud.accountservice.controller;

import com.syrobin.cloud.accountservice.service.AccountService;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-03-01 10:50 AM
 */
@RestController
@RequestMapping("account")
public class AccountController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AccountService accountServiceImpl;

    /**
     * 扣减账户余额
     * actionContext save xid
     * @param userId 用户id
     * @param money 金额
     * @return
     */
    @RequestMapping("decrease")
    public boolean prepare(@RequestBody BusinessActionContext actionContext, @RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money){
        return accountServiceImpl.decrease(actionContext.getXid(), userId,money);
    }

    @RequestMapping("commit")
    public boolean commit(@RequestBody BusinessActionContext actionContext){
        try {
            return accountServiceImpl.commit(actionContext.getXid());
        }catch (IllegalStateException e){
            logger.error("commit error:", e);
            return true;
        }
    }

    @RequestMapping("rollback")
    public boolean rollback(@RequestBody BusinessActionContext actionContext){
        try {
            return accountServiceImpl.rollback(actionContext.getXid());
        }catch (IllegalStateException e){
            logger.error("rollback error:", e);
            return true;
        }
    }
}
