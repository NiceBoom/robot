package com.fly.robot.pojo;

import java.util.List;

public class GetFlyBookMsgReceiveDTO {
    private String schema;//事件模式
    private Header header;//事件头
    private Event event;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    //事件头
    public static class Header {
        private String event_id;//事件ID
        private String event_type;//事件类型
        private String create_time;//事件创建的时间戳(单位：毫秒)
        private String token;//事件Token
        private String app_id;//应用ID
        private String tenant_key;//租户key

        public String getEvent_id() {
            return event_id;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public String getEvent_type() {
            return event_type;
        }

        public void setEvent_type(String event_type) {
            this.event_type = event_type;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getTenant_key() {
            return tenant_key;
        }

        public void setTenant_key(String tenant_key) {
            this.tenant_key = tenant_key;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "event_id='" + event_id + '\'' +
                    ", event_type='" + event_type + '\'' +
                    ", create_time='" + create_time + '\'' +
                    ", token='" + token + '\'' +
                    ", app_id='" + app_id + '\'' +
                    ", tenant_key='" + tenant_key + '\'' +
                    '}';
        }
    }

    public static class Event {
        private Sender sender;//事件的发送者
        private Message message;//事件中包含的消息内容

        public Sender getSender() {
            return sender;
        }

        public void setSender(Sender sender) {
            this.sender = sender;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public static class Sender {
            private SenderId sender_id;//用户ID
            private String sender_type;//消息发送者类型，目前只支持用户(user)发送的消息
            private String tenant_key;//tenant key 租户在飞书上的唯一标识，用来换取对应的tenant_access_token，也可以用作租户在应用里的唯一标识

            public SenderId getSender_id() {
                return sender_id;
            }

            public void setSender_id(SenderId sender_id) {
                this.sender_id = sender_id;
            }

            public String getSender_type() {
                return sender_type;
            }

            public void setSender_type(String sender_type) {
                this.sender_type = sender_type;
            }

            public String getTenant_key() {
                return tenant_key;
            }

            public void setTenant_key(String tenant_key) {
                this.tenant_key = tenant_key;
            }

            public static class SenderId {
                private String union_id;//用户的union id
                private String user_id;//用户的user id
                private String open_id;//用户的open id

                public String getUnion_id() {
                    return union_id;
                }

                public void setUnion_id(String union_id) {
                    this.union_id = union_id;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getOpen_id() {
                    return open_id;
                }

                public void setOpen_id(String open_id) {
                    this.open_id = open_id;
                }

                @Override
                public String toString() {
                    return "SenderId{" +
                            "union_id='" + union_id + '\'' +
                            ", user_id='" + user_id + '\'' +
                            ", open_id='" + open_id + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "Sender{" +
                        "sender_id=" + sender_id +
                        ", sender_type='" + sender_type + '\'' +
                        ", tenant_key='" + tenant_key + '\'' +
                        '}';
            }
        }

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

            public String getMessage_id() {
                return message_id;
            }

            public void setMessage_id(String message_id) {
                this.message_id = message_id;
            }

            public String getRoot_id() {
                return root_id;
            }

            public void setRoot_id(String root_id) {
                this.root_id = root_id;
            }

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getChat_id() {
                return chat_id;
            }

            public void setChat_id(String chat_id) {
                this.chat_id = chat_id;
            }

            public String getChat_type() {
                return chat_type;
            }

            public void setChat_type(String chat_type) {
                this.chat_type = chat_type;
            }

            public String getMessage_type() {
                return message_type;
            }

            public void setMessage_type(String message_type) {
                this.message_type = message_type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public List<Mention> getMentions() {
                return mentions;
            }

            public void setMentions(List<Mention> mentions) {
                this.mentions = mentions;
            }

            public static class Mention {
                private String key;//mention key
                private Id id;//用户ID
                private String name;//用户姓名
                private String tenant_key;//tenant key，为租户在飞书上的唯一标识，用来换取对应的tenant_access_token，也可以用作租户在应用里面的唯一标识

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public Id getId() {
                    return id;
                }

                public void setId(Id id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getTenant_key() {
                    return tenant_key;
                }

                public void setTenant_key(String tenant_key) {
                    this.tenant_key = tenant_key;
                }

                public static class Id {
                    private String union_id;//用户的union id
                    private String user_id;//用户的user id
                    private String open_id;//用户的open id

                    public String getUnion_id() {
                        return union_id;
                    }

                    public void setUnion_id(String union_id) {
                        this.union_id = union_id;
                    }

                    public String getUser_id() {
                        return user_id;
                    }

                    public void setUser_id(String user_id) {
                        this.user_id = user_id;
                    }

                    public String getOpen_id() {
                        return open_id;
                    }

                    public void setOpen_id(String open_id) {
                        this.open_id = open_id;
                    }

                    @Override
                    public String toString() {
                        return "Id{" +
                                "union_id='" + union_id + '\'' +
                                ", user_id='" + user_id + '\'' +
                                ", open_id='" + open_id + '\'' +
                                '}';
                    }
                }

                @Override
                public String toString() {
                    return "Mention{" +
                            "key='" + key + '\'' +
                            ", id=" + id +
                            ", name='" + name + '\'' +
                            ", tenant_key='" + tenant_key + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "Message{" +
                        "message_id='" + message_id + '\'' +
                        ", root_id='" + root_id + '\'' +
                        ", parent_id='" + parent_id + '\'' +
                        ", create_time='" + create_time + '\'' +
                        ", chat_id='" + chat_id + '\'' +
                        ", chat_type='" + chat_type + '\'' +
                        ", message_type='" + message_type + '\'' +
                        ", content='" + content + '\'' +
                        ", mentions=" + mentions +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Event{" +
                    "sender=" + sender +
                    ", message=" + message +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetFlyBookMsgReceiveDTO{" +
                "schema='" + schema + '\'' +
                ", header=" + header +
                ", event=" + event +
                '}';
    }
}


