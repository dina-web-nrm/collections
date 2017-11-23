/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import se.nrm.dina.collections.exceptions.utils.ErrorCode;

/**
 *
 * @author idali
 */
@Slf4j
@Dependent
public class ExceptionsHandler implements Serializable {
    
    public ExceptionsHandler() {
        
    }

    public String getRootCauseName(final Throwable throwable) {
        return getRootCause(throwable).getClass().getSimpleName();

    }

    public Throwable getRootCause(final Throwable throwable) {
        final List<Throwable> list = getThrowableList(throwable);
        return list.size() < 2 ? null : (Throwable) list.get(list.size() - 1);
    }

    private List<Throwable> getThrowableList(Throwable throwable) {
        final List<Throwable> list = new ArrayList<>();
        while (throwable != null && list.contains(throwable) == false) {
            list.add(throwable);
            throwable = ExceptionUtils.getCause(throwable);
        }
        return list;
    }

    public String handleHibernateConstraintViolation(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRootCause(e).getMessage());
        sb.append(" [");
        sb.append(getRootCause(e).getClass().getSimpleName());
        sb.append("]");
        return sb.toString();
    }
    
    public String getErrorSource(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRootCause(e).getMessage());
        sb.append(" [");
        sb.append(getRootCause(e).getClass().getSimpleName());
        sb.append("]");
        return sb.toString();
    }
      
    public boolean isHibernateConstraintViolationException(Exception e) {
        return e.getCause() instanceof ConstraintViolationException;
    }
    
    public CollectionsException getCollectionsException(Exception e) {
        if(e.getCause() instanceof ConstraintViolationException) {
            return new CollectionsConstraintViolationException(getErrorSource(e), e.getMessage(), e.getMessage(), ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION_CODE);
        } else {
            return new CollectionsDatabaseException(getErrorSource(e), e.getMessage(), e.getMessage(), ErrorCode.DATABASE_EXCEPTION_CODE);
        }     
    } 
}
