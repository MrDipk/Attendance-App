package com.example.firebase;

class employeeInfo {
    public String id, name, num, vcode, doj, title;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public employeeInfo() {
    }

    public employeeInfo(String id, String name, String num, String vcode, String doj, String title) {

        this.id = id;
        this.name = name;
        this.num = num;
        this.vcode = vcode;
        this.doj = doj;
        this.title = title;
    }
}
