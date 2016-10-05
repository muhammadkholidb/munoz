package hu.pe.munoz.commondata.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.common.helper.CommonConstants;
import hu.pe.munoz.common.helper.PasswordUtils;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import hu.pe.munoz.commondata.dao.UserDao;
import hu.pe.munoz.commondata.dao.UserGroupDao;
import hu.pe.munoz.commondata.dao.UserGroupMenuPermissionDao;
import hu.pe.munoz.commondata.entity.UserEntity;
import hu.pe.munoz.commondata.entity.UserGroupEntity;
import hu.pe.munoz.commondata.entity.UserGroupMenuPermissionEntity;
import hu.pe.munoz.commondata.helper.DataValidation;
import hu.pe.munoz.commondata.helper.Dto;
import hu.pe.munoz.commondata.helper.DtoUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserBoImpl implements UserBo {

    // private static final Logger LOG = LoggerFactory.getLogger(UserBoImpl.class);
    
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserGroupDao userGroupDao;
    
    @Autowired
    private UserGroupMenuPermissionDao userGroupMenuPermissionDao;
    
    @Override
    public List<Dto> getAllUserWithGroup(Dto dtoInput) {
        
        // No validation
        
        List<Object[]> list = userDao.findAllJoinUserGroup();
        List<Dto> listDto = new ArrayList<Dto>();
        for (Object[] objects : list) {
            Dto dtoUser = DtoUtils.toDto(objects[0]);
            Dto dtoUserGroup = DtoUtils.toDto(objects[1]);
            Dto dto = DtoUtils.omit(dtoUser, "password", "salt", "lowerUsername", "lowerEmail", "createdAt", "modifiedAt");
            dto.put("userGroup", DtoUtils.omit(dtoUserGroup, "lowerName", "createdAt", "modifiedAt"));
            listDto.add(dto);
        }
        return listDto;
    }

    @Override
    public List<Dto> getUserListByUserGroupId(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "userGroupId");
        
        // Validate values
        String strUserGroupId = dtoInput.getStringValue("userGroupId");
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");
        
        List<UserEntity> list = userDao.findByUserGroupId(Long.valueOf(strUserGroupId));
        List<Dto> listDto = new ArrayList<Dto>();
        for (UserEntity user : list) {
            Dto dtoUser = DtoUtils.toDto(user);
            Dto dto = DtoUtils.omit(dtoUser, "password", "salt", "lowerUsername", "lowerEmail", "createdAt", "modifiedAt");
            listDto.add(dto);
        }
        return listDto;
    }

    @Override
    public Dto getUserById(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");
        
        // Validate values
        String strUserId = dtoInput.getStringValue("id");
        DataValidation.validateNumeric(strUserId, "User ID");
        
        UserEntity user = userDao.findById(Long.valueOf(strUserId));
        if (user == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }
        
        Dto dtoUser = DtoUtils.toDto(user);
        Dto dto = DtoUtils.omit(dtoUser, "password", "salt", "lowerUsername", "lowerEmail", "createdAt", "modifiedAt");
        return dto;
    }

    @Override
    public Dto login(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "password", "username");
        
        String strPassword = dtoInput.get("password");
        String strUsername = dtoInput.get("username");
        
        Object[] objects = userDao.findOneByEmailOrUsernameJoinUserGroup(strUsername, strUsername);
        if (objects == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }
        
        UserEntity user = (UserEntity) objects[0];
        UserGroupEntity userGroup = (UserGroupEntity) objects[1];
        
        String stirredPassword = PasswordUtils.stirWithSalt(strPassword, user.getSalt());
        
        if (!user.getPassword().equals(stirredPassword)) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }
                
        if (CommonConstants.NO.equals(user.getActive())) {
            throw new DataException(ExceptionCode.E1008, ErrorMessageConstants.CANT_LOGIN_CAUSE_USER_NOT_ACTIVE);
        }

        if (CommonConstants.NO.equals(userGroup.getActive())) {
            throw new DataException(ExceptionCode.E1008, ErrorMessageConstants.CANT_LOGIN_CAUSE_USER_GROUP_NOT_ACTIVE);
        }

        List<UserGroupMenuPermissionEntity> menuPermissions = userGroupMenuPermissionDao.findByUserGroupId(user.getUserGroupId());
        List<Dto> listDtoMenuPermission = new ArrayList<Dto>();
        for (UserGroupMenuPermissionEntity menuPermission : menuPermissions) {
            Dto dtoMenuPermission = DtoUtils.toDto(menuPermission);
            listDtoMenuPermission.add(DtoUtils.omit(dtoMenuPermission, "createdAt", "modifiedAt"));
        }
        
        Dto dtoUser = DtoUtils.toDto(user);
        Dto dtoUserGroup = DtoUtils.toDto(userGroup);
        dtoUserGroup.put("menuPermissions", listDtoMenuPermission);
        
        Dto dto = DtoUtils.omit(dtoUser, "password", "salt", "lowerUsername", "lowerEmail", "createdAt", "modifiedAt");
        dto.put("userGroup", DtoUtils.omit(dtoUserGroup, "lowerName", "createdAt", "modifiedAt"));
        return dto;
    }

    @Override
    public Dto getUserByEmail(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "email");
        
        String strEmail = dtoInput.get("email");
        
        // Validate values
        DataValidation.validateEmail(strEmail);
        
        UserEntity user = userDao.findOneByEmail(strEmail);
        if (user == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND, new Object[]{strEmail});
        }
        
        Dto dtoUser = DtoUtils.toDto(user);
        Dto dto = DtoUtils.omit(dtoUser, "password", "salt", "lowerUsername", "lowerEmail", "createdAt", "modifiedAt");
        return dto;
    }

    @Override
    public Dto getUserByUsername(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "username");
        
        String strUsername = dtoInput.get("username");
        
        // Validate values
        DataValidation.validateUsername(strUsername);
        
        UserEntity user = userDao.findOneByUsername(strUsername);
        if (user == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND, new Object[]{strUsername});
        }
        
        Dto dtoUser = DtoUtils.toDto(user);
        Dto dto = DtoUtils.omit(dtoUser, "password", "salt", "lowerUsername", "lowerEmail", "createdAt", "modifiedAt");
        return dto;
    }

    @Override
    public Dto addUser(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "firstName", "email", "username", "password", "active", "userGroupId");
        
        String strFirstName = dtoInput.get("firstName");
        String strLastName = dtoInput.get("lastName");
        String strEmail = dtoInput.get("email");
        String strUsername = dtoInput.get("username");
        String strPassword = dtoInput.get("password");
        String strActive = dtoInput.get("active");
        String strUserGroupId = dtoInput.getStringValue("userGroupId");
        
        // Validate values
        DataValidation.validateEmpty(strFirstName, "First Name");
        DataValidation.validateEmpty(strPassword, "Password");
        DataValidation.validateEmail(strEmail);
        DataValidation.validateUsername(strUsername);
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");
        DataValidation.validateYesNo(strActive, "Active");
        
        UserEntity findUser = userDao.findOneByEmailOrUsername(strEmail, strUsername);
        if (findUser != null) {
            // Chek username
            if (findUser.getLowerUsername().equals(strUsername.toLowerCase())) {
                throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[] {strUsername});
            }
            // Chek email
            if (findUser.getLowerEmail().equals(strEmail.toLowerCase())) {
                throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[] {strEmail});
            }
        }
        
        // Find user group by ID
        UserGroupEntity findUserGroupById = userGroupDao.findById(Long.valueOf(strUserGroupId));
        if (findUserGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        String salt = RandomStringUtils.randomAlphanumeric(32);
        String stirredPassword = PasswordUtils.stirWithSalt(strPassword, salt);
        
        UserEntity addUser = new UserEntity();
        addUser.setFirstName(strFirstName);
        addUser.setLastName(strLastName);
        addUser.setEmail(strEmail);
        addUser.setUsername(strUsername);
        addUser.setPassword(stirredPassword);
        addUser.setSalt(salt);
        addUser.setActive(strActive.toLowerCase());
        addUser.setUserGroupId(Long.valueOf(strUserGroupId));
        addUser.setLowerUsername(strUsername.toLowerCase());
        addUser.setLowerEmail(strEmail.toLowerCase());
        
        UserEntity added = userDao.insert(addUser);
        
        Dto dtoUserAdded = DtoUtils.toDto(added);
        Dto dto = DtoUtils.omit(dtoUserAdded, "password", "salt", "lowerUsername", "lowerEmail", "createdAt", "modifiedAt");
        return dto;
    }

    @Override
    public Dto editUser(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id", "firstName", "email", "username", "active", "userGroupId");
        
        String strId = dtoInput.getStringValue("id");
        String strFirstName = dtoInput.get("firstName");
        String strLastName = dtoInput.get("lastName");
        String strEmail = dtoInput.get("email");
        String strUsername = dtoInput.get("username");
        String strPassword = dtoInput.get("password");
        String strActive = dtoInput.get("active");
        String strUserGroupId = dtoInput.getStringValue("userGroupId");
        
        // Validate values
        DataValidation.validateNumeric(strId, "User ID");
        DataValidation.validateEmpty(strFirstName, "First Name");
        DataValidation.validateEmail(strEmail);
        DataValidation.validateUsername(strUsername);
        DataValidation.validateNumeric(strUserGroupId, "User Group ID");
        DataValidation.validateYesNo(strActive, "Active");
        
        UserEntity findUserById = userDao.findById(Long.valueOf(strId));
        if (findUserById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }
        
        // Find other user with specified email and username
        UserEntity findUserByUsernameEmail = userDao.findOneByEmailOrUsername(strEmail, strUsername);
        if ((findUserByUsernameEmail != null) && (!Objects.equals(findUserByUsernameEmail.getId(), Long.valueOf(strId)))) {
            // Chek username
            if (findUserByUsernameEmail.getLowerUsername().equals(strUsername.toLowerCase())) {
                throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[] {strUsername});
            }
            // Chek email
            if (findUserByUsernameEmail.getLowerEmail().equals(strEmail.toLowerCase())) {
                throw new DataException(ExceptionCode.E1003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[] {strEmail});
            }
        }
        
        // Find user group by ID
        UserGroupEntity findUserGroupById = userGroupDao.findById(Long.valueOf(strUserGroupId));
        if (findUserGroupById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_GROUP_NOT_FOUND);
        }

        findUserById.setFirstName(strFirstName);
        findUserById.setLastName(strLastName);
        findUserById.setUsername(strUsername);
        findUserById.setEmail(strEmail);
        findUserById.setActive(strActive.toLowerCase());
        findUserById.setUserGroupId(Long.valueOf(strUserGroupId));
        findUserById.setLowerEmail(strEmail.toLowerCase());
        findUserById.setLowerUsername(strUsername.toLowerCase());
        
        if ((strPassword != null) && !strPassword.trim().isEmpty()) {
            String stirredPassword = PasswordUtils.stirWithSalt(strPassword, findUserById.getSalt());
            findUserById.setPassword(stirredPassword);
        }
        
        UserEntity updated = userDao.update(findUserById);
        
        Dto dtoUserUpdated = DtoUtils.toDto(updated);
        Dto dto = DtoUtils.omit(dtoUserUpdated, "password", "salt", "lowerUsername", "lowerEmail", "createdAt", "modifiedAt");
        return dto;
    }

    @Override
    public void removeUser(Dto dtoInput) throws DataException {
        
        // Validate dtoInput
        DataValidation.containsRequiredData(dtoInput, "id");
        
        // Validate values
        String strUserId = dtoInput.getStringValue("id");
        DataValidation.validateNumeric(strUserId, "User Id");
        
        UserEntity findUserById = userDao.findById(Long.valueOf(strUserId));
        if (findUserById == null) {
            throw new DataException(ExceptionCode.E1001, ErrorMessageConstants.USER_NOT_FOUND);
        }
        
        userDao.delete(findUserById);
    }

}
