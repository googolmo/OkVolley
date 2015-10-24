package im.amomo.volley.sample.model;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-09
 * Time: 14:02
 */
public class PersonalDes {

    /**
     * loc_id : 108288
     * name : Ailurus
     * created : 2014-04-28 14:08:55
     * is_banned : false
     * is_suicide : false
     * loc_name : 北京
     * avatar : http://img3.douban.com/icon/u88028015-2.jpg
     * signature : 学无止境.
     * uid : Ailurus
     * alt : http://www.douban.com/people/Ailurus/
     * desc : Just Be Unreserved!
     * type : user
     * id : 88028015
     * large_avatar : http://img3.douban.com/icon/up88028015-2.jpg
     */

    private String loc_id;
    private String name;
    private String created;
    private boolean is_banned;
    private boolean is_suicide;
    private String loc_name;
    private String avatar;
    private String signature;
    private String uid;
    private String alt;
    private String desc;
    private String type;
    private String id;
    private String large_avatar;

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setIs_banned(boolean is_banned) {
        this.is_banned = is_banned;
    }

    public void setIs_suicide(boolean is_suicide) {
        this.is_suicide = is_suicide;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLarge_avatar(String large_avatar) {
        this.large_avatar = large_avatar;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public String getName() {
        return name;
    }

    public String getCreated() {
        return created;
    }

    public boolean isIs_banned() {
        return is_banned;
    }

    public boolean isIs_suicide() {
        return is_suicide;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getSignature() {
        return signature;
    }

    public String getUid() {
        return uid;
    }

    public String getAlt() {
        return alt;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getLarge_avatar() {
        return large_avatar;
    }

    @Override
    public String toString() {
        return
                "loc_id : " + loc_id + '\n' +
                "name : " + name + '\n' +
                "created : " + created + '\n' +
                "is_banned : " + is_banned + '\n' +
                "is_suicide : " + is_suicide + '\n' +
                "loc_name : " + loc_name + '\n' +
                "avatar : " + avatar + '\n' +
                "signature : " + signature + '\n' +
                "uid : " + uid + '\n' +
                "alt : " + alt + '\n' +
                "desc : " + desc + '\n' +
                "type : " + type + '\n' +
                "id : " + id + '\n' +
                "large_avatar : " + large_avatar + '\n';
    }
}
