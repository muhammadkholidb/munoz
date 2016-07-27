package hu.pe.munoz.commondata.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.pe.munoz.common.exception.DataException;
import hu.pe.munoz.common.exception.ExceptionCode;
import hu.pe.munoz.commondata.ErrorMessageConstants;
import hu.pe.munoz.commondata.dao.UserDao;
import hu.pe.munoz.commondata.entity.UserEntity;

@Service
@Transactional
public class UserBoImpl implements UserBo {

    @Autowired
    private UserDao userDao;

    @Override
    public List<UserEntity> getAllUser() {
        return userDao.findAllWithUserGroup();
    }

    @Override
    public List<UserEntity> getUserListByUserGroupId(Long userGroupId) {
        return userDao.findByUserGroupId(userGroupId);
    }

    @Override
    public UserEntity getUser(Long id) throws DataException {
        UserEntity user = userDao.findById(id);
        if (user == null) {
            throw new DataException(ExceptionCode.D0001, ErrorMessageConstants.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public UserEntity getUser(String email, String username) throws DataException {
        UserEntity user = userDao.findByEmailOrUsername(email, username);
        if (user == null) {
            throw new DataException(ExceptionCode.D0001, ErrorMessageConstants.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public UserEntity getUserByEmail(String email) throws DataException {
        UserEntity user = userDao.findByEmail(email);
        if (user == null) {
            throw new DataException(ExceptionCode.D0001, ErrorMessageConstants.USER_NOT_FOUND, new Object[]{email});
        }
        return null;
    }

    @Override
    public UserEntity getUserByUsername(String username) throws DataException {
        UserEntity user = userDao.findByUsername(username);
        if (user == null) {
            throw new DataException(ExceptionCode.D0001, ErrorMessageConstants.USER_NOT_FOUND, new Object[]{username});
        }
        return null;
    }

    @Override
    public UserEntity addUser(UserEntity userEntity) throws DataException {
        UserEntity findUser = userDao.findByEmailOrUsername(userEntity.getEmail(), userEntity.getUsername());
        if (findUser != null) {
            // Chek username
            String userUsername = userEntity.getUsername();
            if (findUser.getUsernameUpper().equals(userUsername.toUpperCase())) {
                throw new DataException(ExceptionCode.D0003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[]{userUsername});
            }
            // Chek email
            String userEmail = userEntity.getEmail();
            if (findUser.getEmailUpper().equals(userEmail.toUpperCase())) {
                throw new DataException(ExceptionCode.D0003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[]{userEmail});
            }
        }
        return userDao.insert(userEntity);
    }

    @Override
    public UserEntity editUser(UserEntity userEntity) throws DataException {
        UserEntity findUserById = userDao.findById(userEntity.getId());
        if (findUserById == null) {
            throw new DataException(ExceptionCode.D0001, ErrorMessageConstants.USER_NOT_FOUND);
        }
        UserEntity findUserByUsernameEmail = userDao.findByEmailOrUsername(userEntity.getEmail(), userEntity.getUsername());
        if ((findUserByUsernameEmail != null) && (findUserByUsernameEmail.getId() != userEntity.getId())) {
            // Chek username
            String userUsername = userEntity.getUsername();
            if (findUserByUsernameEmail.getUsernameUpper().equals(userUsername.toUpperCase())) {
                throw new DataException(ExceptionCode.D0003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_USERNAME, new Object[]{userUsername});
            }
            // Chek email
            String userEmail = userEntity.getEmail();
            if (findUserByUsernameEmail.getEmailUpper().equals(userEmail.toUpperCase())) {
                throw new DataException(ExceptionCode.D0003, ErrorMessageConstants.USER_ALREADY_EXISTS_WITH_EMAIL, new Object[]{userEmail});
            }
        }
        return userDao.update(userEntity);
    }

    @Override
    public void removeUser(Long id) throws DataException {
        UserEntity findUserById = userDao.findById(id);
        if (findUserById == null) {
            throw new DataException(ExceptionCode.D0001, ErrorMessageConstants.USER_NOT_FOUND);
        }
        try {
            userDao.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
