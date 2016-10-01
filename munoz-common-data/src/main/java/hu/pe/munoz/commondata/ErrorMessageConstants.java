package hu.pe.munoz.commondata;

public class ErrorMessageConstants {

    // Validation
    public static final String REQUIRED_PARAMETERS_NOT_FOUND = "error.commondata.validation.RequiredParametersNotFound";
    public static final String REQUIRED_PARAMETER_NOT_FOUND = "error.commondata.validation.RequiredParameterNotFound";
    public static final String INVALID_EMAIL_ADDRESS = "error.commondata.validation.InvalidEmailAddress";
    public static final String INVALID_NUMERIC = "error.commondata.validation.InvalidNumeric"; // Invalid numeric {0} for data {1}
    public static final String EMPTY_USERNAME = "error.commondata.validation.EmptyUsername";
    public static final String USERNAME_TOO_SHORT = "error.commondata.validation.UsernameTooShort";
    public static final String INVALID_YES_NO = "error.commondata.validation.InvalidYesNo"; // Invalid value {0} for data {1}
    public static final String INVALID_JSON_ARRAY = "error.commondata.validation.InvalidJSONArray";
    public static final String INVALID_JSON_OBJECT = "error.commondata.validation.InvalidJSONObject";
    public static final String EMPTY_VALUE = "error.commondata.validation.EmptyValue"; // The value of {0} cannot be empty
    
    // User Group
    public static final String CANT_REMOVE_USER_GROUP_CAUSE_USER_EXISTS = "error.commondata.CantRemoveUserGroupCauseUserExists";
    public static final String USER_GROUP_NOT_FOUND = "error.commondata.UserGroupNotFound";
    public static final String USER_GROUP_ALREADY_EXISTS = "error.commondata.UserGroupAlreadyExists";

    // System
    public static final String SYSTEM_NOT_FOUND = "error.commondata.SystemNotFound";

    // User
    public static final String USER_ALREADY_EXISTS = "error.commondata.UserAlreadyExists";
    public static final String USER_ALREADY_EXISTS_WITH_EMAIL = "error.commondata.UserAlreadyExistsWithEmail";
    public static final String USER_ALREADY_EXISTS_WITH_USERNAME = "error.commondata.UserAlreadyExistsWithUsername";
    public static final String USER_NOT_FOUND = "error.commondata.UserNotFound";
    public static final String CANT_LOGIN_CAUSE_USER_NOT_ACTIVE = "error.commonrest.CantLoginCauseUserNotActive";
    public static final String CANT_LOGIN_CAUSE_USER_GROUP_NOT_ACTIVE = "error.commonrest.CantLoginCauseUserGroupNotActive";

    // User Group Menu Permission
    public static final String USER_GROUP_MENU_PERMISSION_NOT_FOUND = "error.commondata.UserGroupMenuPermissionNotFound";
    
}
