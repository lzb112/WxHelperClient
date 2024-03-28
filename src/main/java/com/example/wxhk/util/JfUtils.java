package com.example.wxhk.util;

import io.vertx.core.json.JsonObject;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 京粉
 * @author: lzb
 * @date: 2024-03-27
 */
public class JfUtils {
    private static char[] b = {'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    public static String b64encode(byte[] bArr) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 <= bArr.length - 1; i2 += 3) {
            byte[] bArr2 = new byte[4];
            byte b2 = 0;
            for (int i3 = 0; i3 <= 2; i3++) {
                int i4 = i2 + i3;
                if (i4 <= bArr.length - 1) {
//                    System.out.print((int)bArr[i4]+",");
                    bArr2[i3] = (byte) (b2 | ((bArr[i4] & 255) >>> ((i3 * 2) + 2)));
                    b2 = (byte) ((((bArr[i4] & 255) << (((2 - i3) * 2) + 2)) & 255) >>> 2);
                } else {
                    bArr2[i3] = b2;
                    b2 = 64;
                }
            }
            bArr2[3] = b2;
            for (int i5 = 0; i5 <= 3; i5++) {
                if (bArr2[i5] <= 63) {
                    sb.append(b[bArr2[i5]]);
                } else {
                    sb.append('=');
                }
            }
        }
        return sb.toString();
    }

    public static String signature(String url, String body, String key) {
        HttpUrl parse = HttpUrl.parse(url);
        if (parse != null) {
            Set<String> queryParameterNames = parse.queryParameterNames();
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            linkedHashSet.addAll(queryParameterNames);
            if (!queryParameterNames.contains("body")) {
                linkedHashSet.add("body");
            }
            if (linkedHashSet.size() > 0) {
                TreeSet treeSet = new TreeSet();
                Iterator it = linkedHashSet.iterator();
                while (it.hasNext()) {
                    treeSet.add((String) it.next());
                }
                StringBuffer stringBuffer = new StringBuffer();
                Iterator it2 = treeSet.iterator();
                while (it2.hasNext()) {
                    String obj = it2.next().toString();
                    String queryParameter = parse.queryParameter(obj);
                    if (Objects.equals(obj, "body") && StringUtils.isEmpty(queryParameter) && !StringUtils.isEmpty(body) && !Objects.equals("{}", body)) {
                        queryParameter = body;
                    }
                    if (!StringUtils.isEmpty(queryParameter)) {
                        stringBuffer.append(queryParameter);
                        stringBuffer.append("&");
                    }
                }
                String stringBuffer2 = stringBuffer.toString();
                if (stringBuffer2.endsWith("&") && stringBuffer2.length() > 1) {
                    stringBuffer2 = stringBuffer2.substring(0, stringBuffer2.length() - 1);
                }
//                System.out.println(stringBuffer2.toString());

//                String s="1b41f155dc6a3a99&jf_app&{\"clientPageId\":\"jingfen_app\",\"funName\":\"getSuperClickUrl\",\"param\":{\"materialInfo\":\"不懂的看这里❗\\n需要在京东app复制以下文案，发客服打开链接下单\\nhttps://coupon.m.jd.com/coupons/show.action?key=c9m6c9sbodad4c08a5cb3d1184a1bf73&roleId=144688696&to=https://mall.jd.com/index-1000013402.html\\n领这个优惠券❗\\n\\n领一张200-20神券 https://u.jd.com/RzBBBHJ\\n\\nhttps://u.jd.com/RiEcW4H\\n加入购物车2件\\n\\nhttps://u.jd.com/RqRnFcM\\n页面领25券\\n加入购物车2件\\n\\n2件高端安慕希\\n2安慕希12盒装\\n2+2=4一起下单\\n付款147返款151\\n\\n下单不要付款，\\n返回等几分钟有送满减支付\\n\\n暮色京东深圳地址\\n（盾的火爆的飞区下+那个权益0.01元一起下单破盾）\"},\"pin\":\"u_4f13b0f0e92bf\",\"unionId\":\"2019612853\"}&20220509&android&3.13.14&Xiaomi&Mi10&ConvertSuperLink&13&71737558&2206*1080&1711213069859&00000000-0000-0000-ffff-ffffc504c6a3";

                String HMACSHA256 = HMACSHA256(stringBuffer2.toString().getBytes(), key.getBytes());
                return HMACSHA256;
            }
        }
        return null;
    }

    private static String HMACSHA256(byte[] bArr, byte[] bArr2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            return byte2hex(mac.doFinal(bArr));
        } catch (InvalidKeyException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static String byte2hex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; bArr != null && i2 < bArr.length; i2++) {
            String hexString = Integer.toHexString(bArr[i2] & 255);
            if (hexString.length() == 1) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        return sb.toString().toLowerCase();
    }

    @SneakyThrows
    public static String changeUrl(String ctx){

        String timestamp = String.valueOf(new Date().getTime());
        JsonObject ep = new JsonObject("{\"hdid\": \"JM9F1ywUPwflvMIpYPok0tt5k9kW4ArJEU3lfLhxBqw=\", \"ts\": null, \"ridx\": -1,\n" +
                "          \"cipher\": {\"d_model\": \"JWunCK==\", \"osVersion\": \"CJC=\", \"partner\": \"DzO3Czc1DJq=\", \"build\": \"CtKyCtK1CNu=\",\n" +
                "                     \"appid\": \"awZpYXLm\", \"client\": \"YW5ucw9fZK==\", \"d_brand\": \"WQvrb21f\", \"screen\": \"CtSmDsenCNqm\",\n" +
                "                     \"clientVersion\": \"Cy4nCy4nDK==\", \"uuid\": \"CNKmCNKmCNKjCNKmCM0mCNKmBWZwZwYjZwZwZwC1CNHtDwOz\",\n" +
                "                     \"androidId\": \"CWS0CWYnDJVuYzZrC2O5EG==\"}, \"ciphertype\": 5, \"version\": \"1.2.0\",\n" +
                "          \"appname\": \"com.jd.jxj\"}");
        ep.put("ts", Long.valueOf(timestamp));
        String signUrl = "https://api.m.jd.com/api?functionId=ConvertSuperLink&appid=jf_app&t="+timestamp+"&clientVersion=3.13.14&build=20220509&client=android&d_brand=Xiaomi&d_model=Mi10&osVersion=13&screen=2206*1080&partner=71737558&androidId=1b41f155dc6a3a99&uuid=00000000-0000-0000-ffff-ffffc504c6a3";
        String key = "53b0dc1fea2b46ef9651e324ddb1f5b2";
        JsonObject body = new JsonObject("{\n" +
                "        \"clientPageId\": \"jingfen_app\",\n" +
                "        \"funName\": \"getSuperClickUrl\",\n" +
                "        \"param\": {\n" +
                "            \"materialInfo\": null\n" +
                "        },\n" +
                "        \"pin\": \"u_4f13b0f0e92bf\",\n" +
                "        \"unionId\": \"2019612853\"\n" +
                "    }");
        body.getJsonObject("param").put("materialInfo", ctx);
//        System.out.println("body："+body.encodePrettily());
        System.out.println(signUrl);
        System.out.println(body);
        System.out.println(key);

        String signature = signature(signUrl, body.toString(), key);

        JsonObject param = new JsonObject("{\n" +
                "        \"hdid\": \"JM9F1ywUPwflvMIpYPok0tt5k9kW4ArJEU3lfLhxBqw=\", \n" +
                "        \"ts\": null, \n" +
                "        \"ridx\": -1, \n" +
                "        \"cipher\": {\n" +
                "            \"body\": null\n" +
                "        },\n" +
                "        \"ciphertype\": 5, \n" +
                "        \"version\": \"1.2.0\", \n" +
                "        \"appname\": \"com.jd.jxj\" \n" +
                "    }");
        param.put("ts", Long.valueOf(timestamp));
        param.getJsonObject("cipher").put("body", b64encode(body.toString().getBytes("utf-8")));
        System.out.println(param);
        System.out.println(signature);
        String url = "https://api.m.jd.com/api?functionId=ConvertSuperLink&t="+timestamp+"&sign="+signature+"&ep={ep}&ef=1";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpHeaders heads=new HttpHeaders();
//        heads.setContentType(MediaType.MULTIPART_FORM_DATA);
        heads.add("J-E-H", "%7B%22hdid%22%3A%22JM9F1ywUPwflvMIpYPok0tt5k9kW4ArJEU3lfLhxBqw%3D%22%2C%22ts%22%3A1711257115611%2C%22ridx%22%3A-1%2C%22cipher%22%3A%7B%22user-agent%22%3A%22dwVyc2vlbs8zBtOzBtO0E2T1aWnuBzSmCtSmDJK5E2f4as8zBtOzBtO0E3DtcwVvbs8nCNqmoNSyCNY7b3ClCJC7%22%7D%2C%22ciphertype%22%3A5%2C%22version%22%3A%221.2.0%22%2C%22appname%22%3A%22com.jd.jxj%22%7D");
        heads.add("J-E-C", "%7B%22hdid%22%3A%22JM9F1ywUPwflvMIpYPok0tt5k9kW4ArJEU3lfLhxBqw%3D%22%2C%22ts%22%3A1711257116687%2C%22ridx%22%3A-1%2C%22cipher%22%3A%7B%22wskey%22%3A%22GUPAbP91YXPLHUDpUxLyWV9ACzdfX1ZOH2HFDWmnJwDUCWvRJWZIGzSnJvHRdXvCGvqzDOrHCJq3Cu9FDs1RCOSnIuVZWOdPVOfnbtC5DXS1CxfpJNLFU0d0J0vUCXUn%22%2C%22pin%22%3A%22dV80ZtOzYtLwCQU5CwTw%22%2C%22whwswswws%22%3A%22IuGmCJSnEWPuYVTQDOO5btO4ctKnDzOnCtU3CJO2DtOmCNZVVtH1WPHYYWHSEVDjZQrvHw51CwnkHxTTJwZsb3VDdzdNVWPmDwjeItvsIxdWYWDBBW1nJNVEcPV1I1PGGXT4C1rYX0VFVXqzb2frZ2VmDXLTZ2rUZzLeDtC0D2d%2BGuPmWQVPEQ5YYwV0Gy1mUxVIC21BCtHNVXT1WOTzdxv1ZwZ1ZUPzIO85oOenUQDia0jZG0r3Uwu3aW52X2P0UxruWvLvI0jUbq%3D%3D%22%2C%22jxjpin%22%3A%22dV80ZtOzYtLwCQU5CwTw%22%7D%2C%22ciphertype%22%3A5%2C%22version%22%3A%221.2.0%22%2C%22appname%22%3A%22com.jd.jxj%22%7D");
        params.add("body", param.toString());
        params.add("bef", "1");
        System.out.println(url);
        System.out.println(params.toString());
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("ep", ep.toString());
        HttpEntity<MultiValueMap> request  = new HttpEntity<>(params,heads);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class, maps);
        JsonObject res = new JsonObject(response.getBody());
        System.out.println(res.toString());
        int code= res.getInteger("code");
        String rs = null;
        if (code == 105){
            Pattern compile = Pattern.compile("\\[(.+)\\]");
            Matcher message = compile.matcher(res.getString("message"));
            message.find();
            String uuid = UUID.randomUUID().toString();
            ctx = ctx.replaceAll(Pattern.quote(message.group(1)), uuid);
            rs = changeUrl(ctx);
            rs = rs.replaceAll(uuid, message.group(1));
        } else if (code != 0) {

        } else{
            rs = res.getJsonObject("data").getString("originalContext");
        }
        System.out.println(rs);
        return rs;
    }

    public static void main(String[] args) {
        changeUrl("不懂的看这里❗\n需要在京东app复制以下文案，发客服打开链接下单\nhttps://coupon.m.jd.com/coupons/show.action?key=c9m6c9sbodad4c08a5cb3d1184a1bf73&roleId=144688696&to=https://mall.jd.com/index-1000013402.html\n领这个优惠券❗\n\n领一张200-20神券 https://u.jd.com/RzBBBHJ\n\nhttps://u.jd.com/RiEcW4H\n加入购物车2件\n\nhttps://u.jd.com/RqRnFcM\n页面领25券\n加入购物车2件\n\n2件高端安慕希\n2安慕希12盒装\n2+2=4一起下单\n付款147返款151\n\n下单不要付款，\n返回等几分钟有送满减支付\n\n暮色京东深圳地址\n（盾的火爆的飞区下+那个权益0.01元一起下单破盾）");
    }
}
