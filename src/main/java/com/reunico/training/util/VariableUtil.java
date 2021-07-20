package com.reunico.training.util;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;

import java.util.Objects;

public class VariableUtil extends Variables {

    /*
     Используется для работы с обхектами в переменных процесса
     */


    public static void setObjectVariable(DelegateExecution execution, String key, Object value) {
        ObjectValue typedValue = objectValue(value)
                .serializationDataFormat(SerializationDataFormats.JSON)
                .create();

        execution.setVariable(key, typedValue);
    }

    public static <T> T getObjectVariable(DelegateExecution execution, String key, Class<T> type) {
        ObjectValue variableTyped = execution.getVariableTyped(key);
        if (Objects.isNull(variableTyped)) {
            return null;
        }
        return type.cast(variableTyped.getValue());
    }
}
