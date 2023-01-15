package com.bbs.remark.bot;

import com.bbs.remark.common.utils.DateUtils;
import com.bbs.remark.pojo.CustomerActive;
import com.bbs.remark.pojo.enumeration.ActiveTypeEnum;
import com.bbs.remark.pojo.enumeration.StatusEnum;
import com.bbs.remark.service.CustomerActiveService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;
import java.util.List;

/**
 * Created by abc@123.com ON 2020/10/20.
 */
public class SendMsgBot extends TelegramLongPollingBot {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String token;
    private String userName;

    private CustomerActiveService customerActiveService;

    /**
     * 需要有一个默认构造函数
     */
    SendMsgBot() {

    }

    public SendMsgBot(String userName, String token, CustomerActiveService customerActiveService) {
        this.userName = userName;
        this.token = token;
        this.customerActiveService = customerActiveService;
        if (StringUtils.isBlank(userName)) {
            this.userName = "zhangsan1_bot";
        }
        if (StringUtils.isBlank(token)) {
            this.token = "1175400695:AAE4otjcMPcmwT-7alwhohTaD6UhDqQJoD0";
        }
        System.out.println("init SendMsgBot......");

//        autoSend();
    }

    private void autoSend() {
        Long chatId = 682328822L;

        for (int messageId=200; messageId < 300; messageId++) {
            send(0, chatId, "message id _" + messageId);
        }

    }

    public String getBotUsername() {
        // 填写username
        return userName;
    }

    @Override
    public String getBotToken() {
        // 填写token
        return token;
    }

    public void onUpdateReceived(Update update) {
        System.out.println("111111");
    }

    public void onUpdatesReceived(List<Update> updates) {

        for (Update update : updates) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handler(update);
            }
        }
    }

    private void handler(Update update) {
        User user = update.getMessage().getFrom();
        String userName = user.getLastName() + user.getFirstName();
        String msgContent = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        int messageId = update.getMessage().getMessageId();
        if (StringUtils.isBlank(msgContent)) {
            send(messageId, chatId, "亲，需要什么帮助呢？");
        }

        System.out.println(String.format("chatId=%d, messageId=%d", chatId, messageId));

        msgContent = msgContent.trim();
        ActiveTypeEnum activeTypeEnum = ActiveTypeEnum.nameFrom(msgContent);
        String msg;

        send(messageId, chatId, "抱歉老板，奴家是机器人不能处理回复哦，麻烦老板联系美女客服：@gfsutyf4");

        /*switch (activeTypeEnum) {
            case STATUS:
            case VIEW:
                CustomerActive customerActive = new CustomerActive();
                customerActive.setCustId(String.valueOf(user.getId()));
                customerActive.setStatus(StatusEnum.DOING.getVal());
                customerActive.setCurDay(String.valueOf(DateUtils.getCurrentDayNumber()));

                CustomerActive doingActive = customerActiveService.findActiveOne(customerActive);
                if (null == doingActive) {
                    send(messageId, chatId, String.format("%s 您没有进行中的活动哦", userName));
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append("用户:").append(userName).append(System.lineSeparator())
                            .append("用户标识：").append(user.getId()).append(System.lineSeparator())
                            .append("进行中活动：").append(doingActive.getActiveName()).append(System.lineSeparator())
                            .append("活动开始时间：").append(DateUtils.getDateTime(doingActive.getCreateDate())).append(System.lineSeparator())
                            .append("该活动已耗时：").append(DateUtils.DifferTimeStr(new Date(), doingActive.getCreateDate()));
                    send(messageId, chatId, sb.toString());
                }
                break;

            case FREE_ACTIVE:
                msg = hasActive(user);
                if (StringUtils.isNotBlank(msg)) {
                    send(messageId, chatId, msg);
                    return;
                }
                addActive(messageId, chatId, ActiveTypeEnum.FREE_ACTIVE, user);

                break;

            case SMOKING:
                msg = hasActive(user);
                if (StringUtils.isNotBlank(msg)) {
                    send(messageId, chatId, msg);
                    return;
                }
                addActive(messageId, chatId, ActiveTypeEnum.SMOKING, user);

                break;

            case EATING:
                msg = hasActive(user);
                if (StringUtils.isNotBlank(msg)) {
                    send(messageId, chatId, msg);
                    return;
                }
                addActive(messageId, chatId, ActiveTypeEnum.EATING, user);

                break;

            case BIG_TOILET:
                msg = hasActive(user);
                if (StringUtils.isNotBlank(msg)) {
                    send(messageId, chatId, msg);
                    return;
                }
                addActive(messageId, chatId, ActiveTypeEnum.BIG_TOILET, user);

                break;

            case SMALL_TOILET:
                msg = hasActive(user);
                if (StringUtils.isNotBlank(msg)) {
                    send(messageId, chatId, msg);
                    return;
                }
                addActive(messageId, chatId, ActiveTypeEnum.SMALL_TOILET, user);

                break;

            case START_WORK:
            case OFF_WORK:
                handlerWork(messageId, chatId, activeTypeEnum, user);
                break;

            case BACK:
                back(messageId, chatId, user);

                break;

            default:
                System.out.println("收到的无效回复内容：" + msgContent);

                // @gfsutyf4

                StringBuffer sb = new StringBuffer();
                sb.append("亲，需要什么帮助呢？").append(System.lineSeparator())
                        .append("可以尝试以下短语哦：").append(System.lineSeparator());
                for (ActiveTypeEnum e : ActiveTypeEnum.values()) {
                    if (e == ActiveTypeEnum.UNKNOW) continue;

                    sb.append(e.getActiveName()).append("、");
                }

                send(messageId, chatId, sb.toString());

                send(messageId, chatId, "我是机器人不能处理回复哦，麻烦亲联系：@gfsutyf4 " );
        }*/

    }

    /**
     * 处理上下班逻辑
     * @param messageId
     * @param chatId
     * @param activeTypeEnum
     * @param user
     */
    private void handlerWork(int messageId, long chatId, ActiveTypeEnum activeTypeEnum, User user) {
        StringBuffer sb = new StringBuffer();

        if (activeTypeEnum != ActiveTypeEnum.START_WORK  && activeTypeEnum != ActiveTypeEnum.OFF_WORK) {

            sb.append("亲，需要什么帮助呢？").append(System.lineSeparator())
                    .append("可以尝试以下短语哦：").append(System.lineSeparator());
            for (ActiveTypeEnum e : ActiveTypeEnum.values()) {
                if (e == ActiveTypeEnum.UNKNOW) continue;

                sb.append(e.getActiveName()).append("、");
            }

            send(messageId, chatId, sb.toString());
            return;
        }

        CustomerActive whereActive = new CustomerActive();
        whereActive.setCurDay(String.valueOf(DateUtils.getCurrentDayNumber()));
        whereActive.setCustId(String.valueOf(user.getId()));

        CustomerActive activeWork = customerActiveService.findActiveWork(whereActive);

        String userName = user.getLastName() + user.getFirstName();

        sb.append("用户:").append(userName).append(System.lineSeparator())
                .append("用户标识：").append(user.getId()).append(System.lineSeparator());

        if (activeTypeEnum == ActiveTypeEnum.START_WORK) {//上班
            if (null == activeWork) {
                int addResult = addWorkActive(messageId, activeTypeEnum, user);
                if (addResult > 0) {
                    sb.append(String.format("签到成功：%s", activeTypeEnum.getActiveName())).append(System.lineSeparator())
                            .append("提示：下班时记得打卡【下班】哦");
                } else {
                    sb.append(String.format("签到失败：%s", activeTypeEnum.getActiveName())).append(System.lineSeparator());
                }

            } else {
                if (activeWork.getStatus() == StatusEnum.DONE.getVal()) {
                    sb.append(String.format("上班时间：%s", DateUtils.dateFormat(activeWork.getCreateDate()))).append(System.lineSeparator())
                            .append(String.format("下班时间：%s", DateUtils.dateFormat(activeWork.getUpdateDate()))).append(System.lineSeparator())
                    .append("提示：今天已经打过下班卡了哦，明天记得及时打卡。");
                } else {
                    sb.append("已打过上班卡，无需重复打卡").append(System.lineSeparator())
                            .append(String.format("打卡时间：%s", DateUtils.formatDateTime(activeWork.getCreateDate()))).append(System.lineSeparator())
                            .append("提示：下班时记得打卡【下班】哦");
                }
            }
            send(messageId, chatId, sb.toString());

        } else {//下班
            if (null == activeWork) {
                sb.append("提示：别急着下班，咱先打个上班卡，好吗？");
            } else {
                if (activeWork.getStatus() == StatusEnum.DONE.getVal()) {
                    sb.append(String.format("上班时间：%s", DateUtils.dateFormat(activeWork.getCreateDate()))).append(System.lineSeparator())
                            .append(String.format("下班时间：%s", DateUtils.dateFormat(activeWork.getUpdateDate()))).append(System.lineSeparator())
                            .append("提示：今天已经打过下班卡了哦，明天记得及时打卡。");
                } else {
                    Date offWorkTime = new Date();

                    activeWork.setStatus(StatusEnum.DONE.getVal());
                    activeWork.setUpdateDate(offWorkTime);
                    activeWork.setCurUseSecond(DateUtils.DifferTime(offWorkTime, activeWork.getCreateDate()));

                    int offWorkResult = customerActiveService.updateByPrimaryKey(activeWork);

                    if (offWorkResult > 0) {
                        sb.append(String.format("签到成功：%s", activeTypeEnum.getActiveName())).append(System.lineSeparator())
                                .append(String.format("上班时间：%s", DateUtils.dateFormat(activeWork.getCreateDate()))).append(System.lineSeparator())
                                .append(String.format("下班时间：%s", DateUtils.dateFormat(offWorkTime))).append(System.lineSeparator())
                                .append(String.format("上班用时：%s", DateUtils.DifferTimeStr(offWorkTime, activeWork.getCreateDate())));
                    } else {
                        sb.append(String.format("签到失败：%s", activeTypeEnum.getActiveName()));
                    }
                }
            }

            send(messageId, chatId, sb.toString());
        }
    }

    /**
     * 回座
     * @param messageId
     * @param chatId
     * @param user
     */
    private void back(int messageId, long chatId, User user) {

        CustomerActive whereActive = new CustomerActive();
        whereActive.setCustId(String.valueOf(user.getId()));
        whereActive.setStatus(StatusEnum.DOING.getVal());
        whereActive.setCurDay(String.valueOf(DateUtils.getCurrentDayNumber()));

        CustomerActive doingActive = customerActiveService.findActiveOne(whereActive);

        String userName = user.getLastName() + user.getFirstName();

        StringBuffer sb = new StringBuffer();
        sb.append("用户:").append(userName).append(System.lineSeparator())
                .append("用户标识：").append(user.getId()).append(System.lineSeparator());
        if (null == doingActive) {
                    sb.append("您没有进行中的活动，无需回座哦");

            send(messageId, chatId, sb.toString());
        } else {
            Date date = new Date();
            doingActive.setUpdateDate(date);
            doingActive.setStatus(StatusEnum.DONE.getVal());
            doingActive.setCurDay(String.valueOf(DateUtils.getCurrentDayNumber()));
            long diffSecodns = DateUtils.DifferTime(date, doingActive.getCreateDate());
            doingActive.setCurUseSecond(diffSecodns);

            int backResult = customerActiveService.updateByPrimaryKey(doingActive);
            if (backResult > 0) {
                doingActive.setoId(null);
                int count = customerActiveService.count(doingActive);

                sb.append("回座成功：").append(doingActive.getActiveName()).append(System.lineSeparator());
                sb.append("本次活动耗时：").append(DateUtils.DifferTimeStr(diffSecodns)).append(System.lineSeparator());
                sb.append(String.format("本日%s：%s次", doingActive.getActiveName(), count)).append(System.lineSeparator());

                //当天活动总耗时
                long times = customerActiveService.statAllTimesPerDay(doingActive);
                sb.append("本日活动总耗时：").append(DateUtils.DifferTimeStr(times)).append(System.lineSeparator());

            } else {
                sb.append("回座失败：").append(doingActive.getActiveName()).append(System.lineSeparator());
            }

            send(messageId, chatId, sb.toString());
        }
    }

    /**
     * 新增上班签到活动
     * @param messageId
     * @param activeTypeEnum
     * @param user
     * @return
     */
    private int addWorkActive(int messageId, ActiveTypeEnum activeTypeEnum, User user) {
        CustomerActive customerActive = new CustomerActive();
        customerActive.setoId(String.valueOf(System.currentTimeMillis()));
        customerActive.setCustId(String.valueOf(user.getId()));
        customerActive.setCreateDate(new Date());
        customerActive.setActiveType(activeTypeEnum.getActiveCode());
        customerActive.setActiveName(activeTypeEnum.getActiveName());
        customerActive.setStatus(StatusEnum.DOING.getVal());
        customerActive.setFirstName(user.getFirstName());
        customerActive.setLastName(user.getLastName());
        String fullName = user.getLastName()+user.getFirstName();
        customerActive.setFullName(fullName);
        customerActive.setCurDay(String.valueOf(DateUtils.getCurrentDayNumber()));

        return customerActiveService.insert(customerActive);
    }

    /**
     * 新增活动
     * @param messageId
     * @param chatId
     * @param activeTypeEnum
     * @param user
     * @return
     */
    private void addActive(int messageId, long chatId, ActiveTypeEnum activeTypeEnum, User user) {
        CustomerActive customerActive = new CustomerActive();
        customerActive.setoId(String.valueOf(System.currentTimeMillis()));
        customerActive.setCustId(String.valueOf(user.getId()));
        customerActive.setCreateDate(new Date());
        customerActive.setActiveType(activeTypeEnum.getActiveCode());
        customerActive.setActiveName(activeTypeEnum.getActiveName());
        customerActive.setStatus(StatusEnum.DOING.getVal());
        customerActive.setFirstName(user.getFirstName());
        customerActive.setLastName(user.getLastName());
        String fullName = user.getLastName()+user.getFirstName();
        customerActive.setFullName(fullName);
        customerActive.setCurDay(String.valueOf(DateUtils.getCurrentDayNumber()));

        int result = customerActiveService.insert(customerActive);


        String userName = user.getLastName() + user.getFirstName();

        StringBuffer sb = new StringBuffer();
        sb.append("用户:").append(userName).append(System.lineSeparator())
          .append("用户标识：").append(user.getId()).append(System.lineSeparator());

        if (result > 0) {
            logger.info(String.format("成功新增活动：%s, 用户名称：%s, 用户标识：%s", activeTypeEnum.getActiveName(), fullName, user.getId()));

            CustomerActive whereExist = new CustomerActive();
            whereExist.setCustId(String.valueOf(user.getId()));
            whereExist.setCurDay(String.valueOf(DateUtils.getCurrentDayNumber()));
            whereExist.setActiveType(activeTypeEnum.getActiveCode());

            int existCount = customerActiveService.count(whereExist);

            sb.append(String.format("签到成功：%s", activeTypeEnum.getActiveName())).append(System.lineSeparator())
              .append(String.format("注意：这是您第 %s 次【%s】", existCount, activeTypeEnum.getActiveName())).append(System.lineSeparator())
              .append(String.format("提示：活动完成后请及时【回座】"));
        } else {
            logger.info(String.format("活动新增失败 活动：%s, 用户名称：%s, 用户标识：%s", activeTypeEnum.getActiveName(), fullName, user.getId()));

            sb.append(String.format("签到失败：%s", activeTypeEnum.getActiveName()));
        }

        send(messageId, chatId, sb.toString());

    }

    /**
     * 查看是否有未完成活动，不包括上下班的活动
     * @param user 会话用户
     * @return String 若返回空则没有未完成活动，否则返回活动内容提示文本
     */
    private String hasActive(User user) {
        CustomerActive whereActive = new CustomerActive();
        whereActive.setStatus(StatusEnum.DOING.getVal());
        whereActive.setCurDay(String.valueOf(DateUtils.getCurrentDayNumber()));
        whereActive.setCustId(String.valueOf(user.getId()));

        CustomerActive activeOne = customerActiveService.findActiveOne(whereActive);
        if (null == activeOne) {
            return null;
        }

        String userName = user.getLastName() + user.getFirstName();

        StringBuffer sb = new StringBuffer();
        sb.append("您有以下未完成的活动，请先完成了再进行其他活动哦！").append(System.lineSeparator())
          .append("用户:").append(userName).append(System.lineSeparator())
          .append("用户标识：").append(user.getId()).append(System.lineSeparator())
          .append("进行中活动：").append(activeOne.getActiveName()).append(System.lineSeparator())
          .append("活动开始时间：").append(DateUtils.getDateTime(activeOne.getCreateDate())).append(System.lineSeparator())
          .append("该活动已耗时：").append(DateUtils.DifferTimeStr(new Date(), activeOne.getCreateDate()));

        return sb.toString();
    }

    /**
     * 发送消息
     * @param messageId
     * @param chatId
     * @param msg
     */
    public void sendMessage(int messageId, Long chatId, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId+"");
        message.setText(msg);
        message.setReplyToMessageId(messageId);
        message.setParseMode(ParseMode.HTML);
        message.setReplyMarkup(null);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("发送消息发生异常："+e.getMessage(), e);
        }
    }

    /**
     * 回复消息
     * @param messageId 消息ID
     * @param chatId 聊天ID
     * @param msg 消息内容
     */
    private void send(int messageId, Long chatId, String msg) {
        /*List<KeyboardRow> keyboardRowList = new ArrayList<>();

        //第一排按钮
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(ActiveTypeEnum.SMALL_TOILET.getActiveName());
        keyboardRow.add(ActiveTypeEnum.BIG_TOILET.getActiveName());
        keyboardRow.add(ActiveTypeEnum.SMOKING.getActiveName());
        keyboardRow.add(ActiveTypeEnum.BACK.getActiveName());

        //第二排按钮
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(ActiveTypeEnum.START_WORK.getActiveName());
        keyboardRow2.add(ActiveTypeEnum.OFF_WORK.getActiveName());
        keyboardRow2.add(ActiveTypeEnum.EATING.getActiveName());
        keyboardRow2.add(ActiveTypeEnum.FREE_ACTIVE.getActiveName());

        //第三排按钮
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(ActiveTypeEnum.VIEW.getActiveName());
        keyboardRow3.add(ActiveTypeEnum.STATUS.getActiveName());

        //添加三排按钮
        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow3);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        SendMessage message = new SendMessage();
        message.setChatId(chatId+"");
        message.setText(msg);
        message.setReplyToMessageId(messageId);
        message.setParseMode(ParseMode.HTML);
        message.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("机器人回复消息发生异常："+e.getMessage(), e);
        }*/
    }

    // 获取group内成员
    private void getChatMember() throws TelegramApiException {
        DefaultAbsSender sender = new DefaultAbsSender(new DefaultBotOptions()) {
            @Override
            public String getBotToken() {
                return token;
            }
        };
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId("xxx");
        getChatMember.setUserId(123L);
        ChatMember chatMember = sender.execute(getChatMember);
    }

}
