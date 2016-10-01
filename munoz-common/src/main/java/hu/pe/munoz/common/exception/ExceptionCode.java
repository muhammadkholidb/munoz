package hu.pe.munoz.common.exception;

public enum ExceptionCode {
    
    // E1 = Exception in data
    // E2 = Exception in rest
    // E3 = Exception in web
    
    /**
     * Entity not found
     */
    E1001, 
    
    /**
     * Can't remove cause other entity
     */
    E1002, 
    
    /**
     * Entity already exists
     */
    E1003,

    /**
     * Parameter not found
     */
    E1004,
    
    /**
     * (Validation) invalid data type or format
     */
    E1005,
    
    /**
     * (Validation) invalid data length
     */
    E1006, 
    
    /**
     * Error import dataset
     */
    E1007,
    
    /**
     * Data status not active
     */
    E1008
}
