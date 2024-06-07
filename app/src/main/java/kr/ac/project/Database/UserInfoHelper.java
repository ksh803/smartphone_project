package kr.ac.project.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoHelper {

    private static final String USER_PREFS = "user_prefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_MAJOR = "major";
    private static final String KEY_UNIVERSITY = "university";

    private SharedPreferences preferences;

    public UserInfoHelper(Context context) {
        preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
    }

    public void saveUserInfo(String name, String age, String major, String university) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_MAJOR, major);
        editor.putString(KEY_UNIVERSITY, university);
        editor.apply();
    }

    public UserInfo getUserInfo() {
        String age = preferences.getString(KEY_AGE, "");
        String major = preferences.getString(KEY_MAJOR, "");
        String university = preferences.getString(KEY_UNIVERSITY, "");

        return new UserInfo(age, major, university);
    }

    public void clearUserInfo() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public class UserInfo {


        private String age;
        private String major;
        private String university;

        public UserInfo(String age, String major, String university) {

            this.age = age;
            this.major = major;
            this.university = university;
        }

        public String getAge() {
            return age;
        }

        public String getMajor() {
            return major;
        }

        public String getUniversity() {
            return university;
        }
    }
}