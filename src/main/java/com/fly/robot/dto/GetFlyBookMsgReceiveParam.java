package com.fly.robot.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetFlyBookMsgReceiveParam {
    private String schema;//事件模式
    private Header header;//事件头
    private Event event;

    //事件头
    @Data
    public static class Header {
        private String event_id;//事件ID
        private String event_type;//事件类型
        private String create_time;//事件创建的时间戳(单位：毫秒)
        private String token;//事件Token
        private String app_id;//应用ID
        private String tenant_key;//租户key
    }

    @Data
    public static class Event {
        private Sender sender;//事件的发送者
        private Message message;//事件中包含的消息内容

        @Data
        public static class Sender {
            private SenderId sender_id;//用户ID
            private String sender_type;//消息发送者类型，目前只支持用户(user)发送的消息
            private String tenant_key;//tenant key 租户在飞书上的唯一标识，用来换取对应的tenant_access_token，也可以用作租户在应用里的唯一标识

            @Data
            public static class SenderId {
                private String union_id;//用户的union id
                private String user_id;//用户的user id
                private String open_id;//用户的open id
            }
        }

        @Data
        public static class Message {
            private String message_id;//消息的open_message_id，详细说明：https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message/intro#ac79c1c2
            private String root_id;//根消息id 用于回复消息场景，详细说明：https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message/intro#ac79c1c2
            private String parent_id;//父消息id，用于回复消息场景，详细说明：https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message/intro#ac79c1c2
            private String create_time;//消息发送时间(毫秒)
            private String chat_id;//消息所在的群组ID
            private String chat_type;//消息所在的群组类型 ： 单聊：p2p，群组：group
            private String message_type;//消息类型
            private String content;//消息内容，json格式，详细说明：https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/im-v1/message/events/message_content
            private List<Mention> mentions;//被提及用户的信息

            @Data
            public static class Mention {
                private String key;//mention key
                private Id id;//用户ID
                private String name;//用户姓名
                private String tenant_key;//tenant key，为租户在飞书上的唯一标识，用来换取对应的tenant_access_token，也可以用作租户在应用里面的唯一标识

                @Data
                public static class Id {
                    private String union_id;//用户的union id
                    private String user_id;//用户的user id
                    private String open_id;//用户的open id
                }
            }
        }
    }
}


