//package com.reservoir.core.utils;
//import com.reservoir.entity.admin.User;
//import com.reservoir.vo.LoginVo;
//import com.reservoir.vo.RegisterVo;
//import com.reservoir.vo.UserInfo;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MapperUtils {
//
//
//    /**
//     * Converts a User to a LoginVo.
//     * @param user the User to convert
//     * @return the converted LoginVo
//     */
//    public static LoginVo UserToLoginVo(User user) {
//        LoginVo vo = new LoginVo();
//        vo.setUsername(user.getUsername());
//        vo.setPassword(user.getPassword());
//        return vo;
//    }
//
//    /**
//     * Converts a LoginVo to a User.
//     * @param loginvo the LoginVo to convert
//     * @return the converted User
//     */
//    public static User LoginVoToUser(LoginVo loginvo) {
//        User entity = new User();
//        entity.setUsername(loginvo.getUsername());
//        entity.setPassword(loginvo.getPassword());
//        return entity;
//    }
//
//    /**
//     * Converts a list of User to a list of LoginVo.
//     * @param userList the list of User to convert
//     * @return the list of converted LoginVo
//     */
//    public static List<LoginVo> UserToLoginVo(List<User> userList) {
//        if (userList == null) {
//            return null;
//        }
//        List<LoginVo> voList = new ArrayList<>();
//        for (User user : userList) {
//            voList.add(UserToLoginVo(user));
//        }
//        return voList;
//    }
//
//    /**
//     * Converts a list of LoginVo to a list of User.
//     * @param loginvoList the list of LoginVo to convert
//     * @return the list of converted User
//     */
//    public static List<User> LoginVoToUser(List<LoginVo> loginvoList) {
//        if (loginvoList == null) {
//            return null;
//        }
//        List<User> entityList = new ArrayList<>();
//        for (LoginVo loginvo : loginvoList) {
//            entityList.add(LoginVoToUser(loginvo));
//        }
//        return entityList;
//    }
//
//
//    /**
//     * Converts a User to a RegisterVo.
//     * @param user the User to convert
//     * @return the converted RegisterVo
//     */
//    public static RegisterVo UserToRegisterVo(User user) {
//        RegisterVo vo = new RegisterVo();
//        vo.setUsername(user.getUsername());
//        vo.setPassword(user.getPassword());
//        vo.setEmail(user.getEmail());
//        return vo;
//    }
//
//    /**
//     * Converts a RegisterVo to a User.
//     * @param registervo the RegisterVo to convert
//     * @return the converted User
//     */
//    public static User RegisterVoToUser(RegisterVo registervo) {
//        User entity = new User();
//        entity.setEmail(registervo.getEmail());
//        entity.setPassword(registervo.getPassword());
//        entity.setUsername(registervo.getUsername());
//        return entity;
//    }
//
//    /**
//     * Converts a list of User to a list of RegisterVo.
//     * @param userList the list of User to convert
//     * @return the list of converted RegisterVo
//     */
//    public static List<RegisterVo> UserToRegisterVo(List<User> userList) {
//        if (userList == null) {
//            return null;
//        }
//        List<RegisterVo> voList = new ArrayList<>();
//        for (User user : userList) {
//            voList.add(UserToRegisterVo(user));
//        }
//        return voList;
//    }
//
//    /**
//     * Converts a list of RegisterVo to a list of User.
//     * @param registervoList the list of RegisterVo to convert
//     * @return the list of converted User
//     */
//    public static List<User> RegisterVoToUser(List<RegisterVo> registervoList) {
//        if (registervoList == null) {
//            return null;
//        }
//        List<User> entityList = new ArrayList<>();
//        for (RegisterVo registervo : registervoList) {
//            entityList.add(RegisterVoToUser(registervo));
//        }
//        return entityList;
//    }
//
//
//    /**
//     * Converts a User to a UserInfo.
//     * @param user the User to convert
//     * @return the converted UserInfo
//     */
//    public static UserInfo UserToUserInfo(User user) {
//        UserInfo vo = new UserInfo();
//        vo.setUsername(user.getUsername());
//        vo.setEmail(user.getEmail());
//        vo.setCreatedAt(user.getCreatedAt());
//        vo.setUpdatedAt(user.getUpdatedAt());
//        vo.setDeletedAt(user.getDeletedAt());
//        vo.setRole(user.getRole());
//        return vo;
//    }
//
//    /**
//     * Converts a UserInfo to a User.
//     * @param userinfo the UserInfo to convert
//     * @return the converted User
//     */
//    public static User UserInfoToUser(UserInfo userinfo) {
//        User entity = new User();
//        entity.setUsername(userinfo.getUsername());
//        entity.setEmail(userinfo.getEmail());
//        entity.setCreatedAt(userinfo.getCreatedAt());
//        entity.setUpdatedAt(userinfo.getUpdatedAt());
//        entity.setDeletedAt(userinfo.getDeletedAt());
//        entity.setRole(userinfo.getRole());
//        return entity;
//    }
//
//    /**
//     * Converts a list of User to a list of UserInfo.
//     * @param userList the list of User to convert
//     * @return the list of converted UserInfo
//     */
//    public static List<UserInfo> UserToUserInfo(List<User> userList) {
//        if (userList == null) {
//            return null;
//        }
//        List<UserInfo> voList = new ArrayList<>();
//        for (User user : userList) {
//            voList.add(UserToUserInfo(user));
//        }
//        return voList;
//    }
//
//    /**
//     * Converts a list of UserInfo to a list of User.
//     * @param userinfoList the list of UserInfo to convert
//     * @return the list of converted User
//     */
//    public static List<User> UserInfoToUser(List<UserInfo> userinfoList) {
//        if (userinfoList == null) {
//            return null;
//        }
//        List<User> entityList = new ArrayList<>();
//        for (UserInfo userinfo : userinfoList) {
//            entityList.add(UserInfoToUser(userinfo));
//        }
//        return entityList;
//    }
//
//
//
//}