package com.example.wxhk;

import okhttp3.HttpUrl;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author: lzb
 * @date: 2024-03-24
 */
public class Test {
    private static char[] b = {'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    public static String b64encode(byte[] bArr) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 <= bArr.length - 1; i2 += 3) {
            byte[] bArr2 = new byte[4];
            byte b2 = 0;
            for (int i3 = 0; i3 <= 2; i3++) {
                int i4 = i2 + i3;
                if (i4 <= bArr.length - 1) {
                    System.out.print((int)bArr[i4]+",");
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
                System.out.println(stringBuffer2.toString());

//                String s="1b41f155dc6a3a99&jf_app&{\"clientPageId\":\"jingfen_app\",\"funName\":\"getSuperClickUrl\",\"param\":{\"materialInfo\":\"不懂的看这里❗\\n需要在京东app复制以下文案，发客服打开链接下单\\nhttps://coupon.m.jd.com/coupons/show.action?key=c9m6c9sbodad4c08a5cb3d1184a1bf73&roleId=144688696&to=https://mall.jd.com/index-1000013402.html\\n领这个优惠券❗\\n\\n领一张200-20神券 https://u.jd.com/RzBBBHJ\\n\\nhttps://u.jd.com/RiEcW4H\\n加入购物车2件\\n\\nhttps://u.jd.com/RqRnFcM\\n页面领25券\\n加入购物车2件\\n\\n2件高端安慕希\\n2安慕希12盒装\\n2+2=4一起下单\\n付款147返款151\\n\\n下单不要付款，\\n返回等几分钟有送满减支付\\n\\n暮色京东深圳地址\\n（盾的火爆的飞区下+那个权益0.01元一起下单破盾）\"},\"pin\":\"u_4f13b0f0e92bf\",\"unionId\":\"2019612853\"}&20220509&android&3.13.14&Xiaomi&Mi10&ConvertSuperLink&13&71737558&2206*1080&1711213069859&00000000-0000-0000-ffff-ffffc504c6a3";

                String HMACSHA256 = HMACSHA256(stringBuffer2.toString().getBytes(), key.getBytes());
                url = url + String.format("&sign=%s", HMACSHA256);
            }
        }
        return url;
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




    public static void main(String[] args) throws Exception {
//        String st = "不AA";
//        System.out.println(b64encode(st.getBytes()));
//        GatewaySignatureHelper.signature is called: str=https://api.m.jd.com/api?functionId=ConvertSuperLink&appid=jf_app&t=1711213069859&clientVersion=3.13.14&build=20220509&client=android&d_brand=Xiaomi&d_model=Mi10&osVersion=13&screen=2206*1080&partner=71737558&androidId=1b41f155dc6a3a99&uuid=00000000-0000-0000-ffff-ffffc504c6a3, str2={"clientPageId":"jingfen_app","funName":"getSuperClickUrl","param":{"materialInfo":"不懂的看这里❗\n需要在京东app复制以下文案，发客服打开链接下单\nhttps://coupon.m.jd.com/coupons/show.action?key=c9m6c9sbodad4c08a5cb3d1184a1bf73&roleId=144688696&to=https://mall.jd.com/index-1000013402.html\n领这个优惠券❗\n\n领一张200-20神券 https://u.jd.com/RzBBBHJ\n\nhttps://u.jd.com/RiEcW4H\n加入购物车2件\n\nhttps://u.jd.com/RqRnFcM\n页面领25券\n加入购物车2件\n\n2件高端安慕希\n2安慕希12盒装\n2+2=4一起下单\n付款147返款151\n\n下单不要付款，\n返回等几分钟有送满减支付\n\n暮色京东深圳地址\n（盾的火爆的飞区下+那个权益0.01元一起下单破盾）"},"pin":"u_4f13b0f0e92bf","unionId":"2019612853"}, str3=53b0dc1fea2b46ef9651e324ddb1f5b2
//        GatewaySignatureHelper.signature is called:
//        str=https://api.m.jd.com/api?functionId=ConvertSuperLink&appid=jf_app&t=1711217293413&clientVersion=3.13.14&build=20220509&client=android&d_brand=Xiaomi&d_model=Mi10&osVersion=13&screen=2206*1080&partner=71737558&androidId=1b41f155dc6a3a99&uuid=00000000-0000-0000-ffff-ffffc504c6a3,
//        str2={"clientPageId":"jingfen_app","funName":"getSuperClickUrl","param":{"materialInfo":"不懂的看这里❗\n需要在京东app复制以下文案，发客服打开链接下单\nhttps://coupon.m.jd.com/coupons/show.action?key=c9m6c9sbodad4c08a5cb3d1184a1bf73&roleId=144688696&to=https://mall.jd.com/index-1000013402.html\n领这个优惠券❗\n\n领一张200-20神券 https://u.jd.com/RzBBBHJ\n\nhttps://u.jd.com/RiEcW4H\n加入购物车2件\n\nhttps://u.jd.com/RqRnFcM\n页面领25券\n加入购物车2件\n\n2件高端安慕希\n2安慕希12盒装\n2+2=4一起下单\n付款147返款151\n\n下单不要付款，\n返回等几分钟有送满减支付\n\n暮色京东深圳地址\n（盾的火爆的飞区下+那个权益0.01元一起下单破盾）"},"pin":"u_4f13b0f0e92bf","unionId":"2019612853"},
//        str3=53b0dc1fea2b46ef9651e324ddb1f5b2
        String str1 = "https://api.m.jd.com/api?functionId=ConvertSuperLink&appid=jf_app&t=1711217293413&clientVersion=3.13.14&build=20220509&client=android&d_brand=Xiaomi&d_model=Mi10&osVersion=13&screen=2206*1080&partner=71737558&androidId=1b41f155dc6a3a99&uuid=00000000-0000-0000-ffff-ffffc504c6a3";
        String str2 = "{\"clientPageId\":\"jingfen_app\",\"funName\":\"getSuperClickUrl\",\"param\":{\"materialInfo\":\"不懂的看这里❗\\n需要在京东app复制以下文案，发客服打开链接下单\\nhttps://coupon.m.jd.com/coupons/show.action?key=c9m6c9sbodad4c08a5cb3d1184a1bf73&roleId=144688696&to=https://mall.jd.com/index-1000013402.html\\n领这个优惠券❗\\n\\n领一张200-20神券 https://u.jd.com/RzBBBHJ\\n\\nhttps://u.jd.com/RiEcW4H\\n加入购物车2件\\n\\nhttps://u.jd.com/RqRnFcM\\n页面领25券\\n加入购物车2件\\n\\n2件高端安慕希\\n2安慕希12盒装\\n2+2=4一起下单\\n付款147返款151\\n\\n下单不要付款，\\n返回等几分钟有送满减支付\\n\\n暮色京东深圳地址\\n（盾的火爆的飞区下+那个权益0.01元一起下单破盾）\"},\"pin\":\"u_4f13b0f0e92bf\",\"unionId\":\"2019612853\"}";
        String str3 = "53b0dc1fea2b46ef9651e324ddb1f5b2";
        String ck ="{\"b1\":\"2f0ae20e-2c26-4b04-86e5-2731aba9ed4e\",\"b2\":\"3.2.3_0\",\"b3\":\"2.1\",\"b4\":\"Lvl6ewu9bGHOtYw2hIYqIiy8DQUh8KSsiqOinHa5FtSQxAiypVgvJdse5UKiClveOKZOA0/S03x6k8UtmlatsaCrHIeMi/Fq9ZoMRO8NAx3V7NxfKRSDdUjxFFGpzaYU9ZodDu/csEAWzFyLC95IcEazjo/b7/1i2QQJjDjFHHLYirJf6ZKGGnAdr2i32DVxHyjVWnCCe0a5drWyzJgQEVY854a+nUdc2s8xNTL7sLLUiZdlh53G0yNpifPI/39B3tw6ltwW8PVamDFzRUs50iyyYNNpFdQaABJ+byZwXLtpOqzaJWJf9R5Z1XbuwxuR5kFnrsLkNW2491IQFaoyPVnXaAKpzDJwXfJy2aTeGKOvkUcjrQ==\",\"b5\":\"41f10f9a6bed4f3cd3ecafe501ceac844702adbb\",\"b7\":\"1711257124326\",\"b6\":\"823b076f1f30ad1e6d89f065439a33a7810e08b8\"}";
        System.out.println(signature(str1, str2, str3));;
    }
//123,34,99,108,105,101,110,116,80,97,103,101,73,100,34,58,34,106,105,110,103,102,101,110,95,97,112,112,34,44,34,102,117,110,78,97,109,101,34,58,34,103,101,116,83,117,112,101,114,67,108,105,99,107,85,114,108,34,44,34,112,97,114,97,109,34,58,123,34,109,97,116,101,114,105,97,108,73,110,102,111,34,58,34,19981,25026,30340,30475,36825,37324,10071,92,110,38656,35201,22312,20140,19996,97,112,112,22797,21046,20197,19979,25991,26696,65292,21457,23458,26381,25171,24320,38142,25509,19979,21333,92,110,104,116,116,112,115,58,47,47,99,111,117,112,111,110,46,109,46,106,100,46,99,111,109,47,99,111,117,112,111,110,115,47,115,104,111,119,46,97,99,116,105,111,110,63,107,101,121,61,99,57,109,54,99,57,115,98,111,100,97,100,52,99,48,56,97,53,99,98,51,100,49,49,56,52,97,49,98,102,55,51,38,114,111,108,101,73,100,61,49,52,52,54,56,56,54,57,54,38,116,111,61,104,116,116,112,115,58,47,47,109,97,108,108,46,106,100,46,99,111,109,47,105,110,100,101,120,45,49,48,48,48,48,49,51,52,48,50,46,104,116,109,108,92,110,39046,36825,20010,20248,24800,21048,10071,92,110,92,110,39046,19968,24352,50,48,48,45,50,48,31070,21048,32,104,116,116,112,115,58,47,47,117,46,106,100,46,99,111,109,47,82,122,66,66,66,72,74,92,110,92,110,104,116,116,112,115,58,47,47,117,46,106,100,46,99,111,109,47,82,105,69,99,87,52,72,92,110,21152,20837,36141,29289,36710,50,20214,92,110,92,110,104,116,116,112,115,58,47,47,117,46,106,100,46,99,111,109,47,82,113,82,110,70,99,77,92,110,39029,38754,39046,50,53,21048,92,110,21152,20837,36141,29289,36710,50,20214,92,110,92,110,50,20214,39640,31471,23433,24917,24076,92,110,50,23433,24917,24076,49,50,30418,35013,92,110,50,43,50,61,52,19968,36215,19979,21333,92,110,20184,27454,49,52,55,36820,27454,49,53,49,92,110,92,110,19979,21333,19981,35201,20184,27454,65292,92,110,36820,22238,31561,20960,20998,38047,26377,36865,28385,20943,25903,20184,32,92,110,92,110,26286,33394,20140,19996,28145,22323,22320,22336,92,110,65288,30462,30340,28779,29190,30340,39134,21306,19979,43,37027,20010,26435,30410,48,46,48,49,20803,19968,36215,19979,21333,30772,30462,65289,34,125,44,34,112,105,110,34,58,34,117,95,52,102,49,51,98,48,102,48,101,57,50,98,102,34,44,34,117,110,105,111,110,73,100,34,58,34,50,48,49,57,54,49,50,56,53,51,34,125
//123,34,99,108,105,101,110,116,80,97,103,101,73,100,34,58,34,106,105,110,103,102,101,110,95,97,112,112,34,44,34,102,117,110,78,97,109,101,34,58,34,103,101,116,83,117,112,101,114,67,108,105,99,107,85,114,108,34,44,34,112,97,114,97,109,34,58,123,34,109,97,116,101,114,105,97,108,73,110,102,111,34,58,34,-28,-72,-115,-26,-121,-126,-25,-102,-124,-25,-100,-117,-24,-65,-103,-23,-121,-116,-30,-99,-105,92,110,-23,-100,-128,-24,-90,-127,-27,-100,-88,-28,-70,-84,-28,-72,-100,97,112,112,-27,-92,-115,-27,-120,-74,-28,-69,-91,-28,-72,-117,-26,-106,-121,-26,-95,-120,-17,-68,-116,-27,-113,-111,-27,-82,-94,-26,-100,-115,-26,-119,-109,-27,-68,-128,-23,-109,-66,-26,-114,-91,-28,-72,-117,-27,-115,-107,92,110,104,116,116,112,115,58,47,47,99,111,117,112,111,110,46,109,46,106,100,46,99,111,109,47,99,111,117,112,111,110,115,47,115,104,111,119,46,97,99,116,105,111,110,63,107,101,121,61,99,57,109,54,99,57,115,98,111,100,97,100,52,99,48,56,97,53,99,98,51,100,49,49,56,52,97,49,98,102,55,51,38,114,111,108,101,73,100,61,49,52,52,54,56,56,54,57,54,38,116,111,61,104,116,116,112,115,58,47,47,109,97,108,108,46,106,100,46,99,111,109,47,105,110,100,101,120,45,49,48,48,48,48,49,51,52,48,50,46,104,116,109,108,92,110,-23,-94,-122,-24,-65,-103,-28,-72,-86,-28,-68,-104,-26,-125,-96,-27,-120,-72,-30,-99,-105,92,110,92,110,-23,-94,-122,-28,-72,-128,-27,-68,-96,50,48,48,45,50,48,-25,-91,-98,-27,-120,-72,32,104,116,116,112,115,58,47,47,117,46,106,100,46,99,111,109,47,82,122,66,66,66,72,74,92,110,92,110,104,116,116,112,115,58,47,47,117,46,106,100,46,99,111,109,47,82,105,69,99,87,52,72,92,110,-27,-118,-96,-27,-123,-91,-24,-76,-83,-25,-119,-87,-24,-67,-90,50,-28,-69,-74,92,110,92,110,104,116,116,112,115,58,47,47,117,46,106,100,46,99,111,109,47,82,113,82,110,70,99,77,92,110,-23,-95,-75,-23,-99,-94,-23,-94,-122,50,53,-27,-120,-72,92,110,-27,-118,-96,-27,-123,-91,-24,-76,-83,-25,-119,-87,-24,-67,-90,50,-28,-69,-74,92,110,92,110,50,-28,-69,-74,-23,-85,-104,-25,-85,-81,-27,-82,-119,-26,-123,-107,-27,-72,-116,92,110,50,-27,-82,-119,-26,-123,-107,-27,-72,-116,49,50,-25,-101,-110,-24,-93,-123,92,110,50,43,50,61,52,-28,-72,-128,-24,-75,-73,-28,-72,-117,-27,-115,-107,92,110,-28,-69,-104,-26,-84,-66,49,52,55,-24,-65,-108,-26,-84,-66,49,53,49,92,110,92,110,-28,-72,-117,-27,-115,-107,-28,-72,-115,-24,-90,-127,-28,-69,-104,-26,-84,-66,-17,-68,-116,92,110,-24,-65,-108,-27,-101,-98,-25,-83,-119,-27,-121,-96,-27,-120,-122,-23,-110,-97,-26,-100,-119,-23,-128,-127,-26,-69,-95,-27,-121,-113,-26,-108,-81,-28,-69,-104,32,92,110,92,110,-26,-102,-82,-24,-119,-78,-28,-70,-84,-28,-72,-100,-26,-73,-79,-27,-100,-77,-27,-100,-80,-27,-99,-128,92,110,-17,-68,-120,-25,-101,-66,-25,-102,-124,-25,-127,-85,-25,-120,-122,-25,-102,-124,-23,-93,-98,-27,-116,-70,-28,-72,-117,43,-23,-126,-93,-28,-72,-86,-26,-99,-125,-25,-101,-118,48,46,48,49,-27,-123,-125,-28,-72,-128,-24,-75,-73,-28,-72,-117,-27,-115,-107,-25,-96,-76,-25,-101,-66,-17,-68,-119,34,125,44,34,112,105,110,34,58,34,117,95,52,102,49,51,98,48,102,48,101,57,50,98,102,34,44,34,117,110,105,111,110,73,100,34,58,34,50,48,49,57,54,49,50,56,53,51,34,125,oyTtbQvvbxHGYWdvIWGsEsTgaW5xZwVkX2PmcMSiSwZ1bu5rbWUsEsTxZXHJdXLvcuDiaWDhVXTiSsmscQPyYW0sExisbWP0ZXTfYWnTbwZlStes5BsD5eoM55gO55yB6B+Z6YoC4f2XXQ7fxSNefeRvxAtukgzukTnrcRNvfS3vsBbuk6XukSlwvepweYtllSzvt5RvhgBwxS3wsZFvlSNfu77wtgXukSlvtZVcbwr0dRLzEs8lY291cQ9kBw0kawGkY29jB2DldXLlbxClc2rldy5rY3Hfb24/a2V5FWC5bJZtEXDsb2HrZNHtCNrrDWDsC2GnCJq0YJPsZtczTxTlbQVTZN0nDNG2ENq2EJYwdQ89aRH0cRC6By9jYWniBwfuBwDlbI9fbwHvoM0nCNKmCNOzDNKyBwr0bWncbkwsrks/woI4gkI8wEaNeEWSkEAdv1nkXQ7feebukSNvlAKyCNKjCtNxfZ7vsBqqaRH0cRC6By91BwfuBwDlbI9IouTMGurAXQ5cbwr0dRLzEs8ldI5gZM5tb20lUwvPY1c0IPnk5Ygq5YWv6BIj54wf6B2wCkI7jvnkXQ5edRHmczelB3UkawGkY29jB1TnUw5QY01cbkwrjowdekwsrtS15Ys4XQ7vsgNvraXejA3xsaxelaYy5Bk2XQ5cbtBuk7bfg5txg6/vhexwrZXvkSncbtBvhexwrZXvkSmnCkobukstrVnkCsiyFJJukSNejbpukSlvtZVcbkI7wEailtO0D+s/vEailtO1CVnkXQ7ukSlvtZXukS3efeRuk5twhB7llSncbks/vEWbxkojsoWReEWSrkwIx+acsowKqoa7eoWRt+aUh+I7wMLcbvnk5fgk6Swy5Bgi5Bsc5hon5Zyz5Zym5Z2KXQ7llStxw77xweJxqalxsSbxweJfe57vtBhukSih6YAt5Bsg5f2N55kACM4mCoWPq+I4qEs1j+I4s+WDvooqjEoblk+8sIT9BMTmaW4sEsT1XzHwCJDsCQYmZJuyYwYsBMT1bwvlbuvuStesCtKnEJYnCtq1CyT9
}
//
//-28=FFE4
//-72=FFB8
//1b41f155dc6a3a99&jf_app&{"clientPageId":"jingfen_app","funName":"getSuperClickUrl","param":{"materialInfo":"不懂的看这里❗\n需要在京东app复制以下文案，发客服打开链接下单\nhttps://coupon.m.jd.com/coupons/show.action?key=c9m6c9sbodad4c08a5cb3d1184a1bf73&roleId=144688696&to=https://mall.jd.com/index-1000013402.html\n领这个优惠券❗\n\n领一张200-20神券 https://u.jd.com/RzBBBHJ\n\nhttps://u.jd.com/RiEcW4H\n加入购物车2件\n\nhttps://u.jd.com/RqRnFcM\n页面领25券\n加入购物车2件\n\n2件高端安慕希\n2安慕希12盒装\n2+2=4一起下单\n付款147返款151\n\n下单不要付款，\n返回等几分钟有送满减支付\n\n暮色京东深圳地址\n（盾的火爆的飞区下+那个权益0.01元一起下单破盾）"},"pin":"u_4f13b0f0e92bf","unionId":"2019612853"}&20220509&android&3.13.14&Xiaomi&Mi10&ConvertSuperLink&13&71737558&2206*1080&1711255074409&00000000-0000-0000-ffff-ffffc504c6a3
//1b41f155dc6a3a99&jf_app&{"clientPageId":"jingfen_app","funName":"getSuperClickUrl","param":{"materialInfo":"不懂的看这里❗\n需要在京东app复制以下文案，发客服打开链接下单\nhttps://coupon.m.jd.com/coupons/show.action?key=c9m6c9sbodad4c08a5cb3d1184a1bf73&roleId=144688696&to=https://mall.jd.com/index-1000013402.html\n领这个优惠券❗\n\n领一张200-20神券 https://u.jd.com/RzBBBHJ\n\nhttps://u.jd.com/RiEcW4H\n加入购物车2件\n\nhttps://u.jd.com/RqRnFcM\n页面领25券\n加入购物车2件\n\n2件高端安慕希\n2安慕希12盒装\n2+2=4一起下单\n付款147返款151\n\n下单不要付款，\n返回等几分钟有送满减支付\n\n暮色京东深圳地址\n（盾的火爆的飞区下+那个权益0.01元一起下单破盾）"},"pin":"u_4f13b0f0e92bf","unionId":"2019612853"}&20220509&android&3.13.14&Xiaomi&Mi10&ConvertSuperLink&13&71737558&2206*1080&1711246723992&00000000-0000-0000-ffff-ffffc504c6a3