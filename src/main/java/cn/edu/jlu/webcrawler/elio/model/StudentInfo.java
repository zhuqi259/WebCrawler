package cn.edu.jlu.webcrawler.elio.model;

/**
 * Created by zhuqi259 on 2015/11/8.
 */
public class StudentInfo {
    private Integer id;
    private String username;
    private String userno;
    private String picurl;
    private Integer gender;
    private String department;
    private String major;
    private String teacher;
    private String telphone;
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userno='" + userno + '\'' +
                ", picurl='" + picurl + '\'' +
                ", gender=" + gender +
                ", department='" + department + '\'' +
                ", major='" + major + '\'' +
                ", teacher='" + teacher + '\'' +
                ", telphone='" + telphone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
