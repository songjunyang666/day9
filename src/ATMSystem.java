import javax.jws.soap.SOAPMessageHandlers;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static sun.security.jgss.GSSUtil.login;


public class ATMSystem {
    public static void main(String[] args) {
        //1.准备系统需要的容器对象，用于存储账户对象
        ArrayList<Account> accounts = new ArrayList<>();
        //2.准备系统的首页：登录 开户
        showMain(accounts);


    }

    public static void showMain(ArrayList<Account> accounts) {
        System.out.println("===============欢迎进入首页===================");
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请您输入您想要的操作：");
            System.out.println("1.登录");
            System.out.println("2.开户");
            System.out.println("您可以输入命令了：");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    //登录
                    login(accounts,sc);
                    break;
                case 2:
                    //开户
                    register(accounts, sc);
                    break;
                default:
                    System.out.println("您当前输入的命令不被支持！");
            }
        }

    }
  //完成用户登录
    private static void login(ArrayList<Account> accounts,Scanner sc) {
        //必须系统中存在账号才可以登录
        if(accounts.size()==0)
            //没有任何账户
        {
            System.out.println("当前系统中无任何账户，您需要先注册！");
        }
        //2.让用户录入卡号，根据卡号查询账户对象。
        while (true) {
            System.out.println("请您输入登录的卡号：");
            String cardId= sc.next();
            //根据卡号查账户对象
            Account acc= getAccountByCardId(cardId,accounts);
            //3.判断账户对象是否存在，存在说明卡号没问题
            if(acc!=null){
                while (true) {
                    //4.让用户继续输入密码
                    System.out.println("请你输入登录的密码：");
                    String password= sc.next();
                    //5.判断密码是否正确
                    if(acc.getPassWord().equals(password)){
                        //密码正确登录成功
                        //展示系统登录后的操作页面（下节课继续完成的功能）！
                        System.out.println("恭喜您，"+acc.getUserName()+"先生/女士成功进入系统，您的卡号是："+acc.getCardId());
                        //展示操作页面
                        showUserCommand(sc,acc,accounts);
                        return;//继续结束登录方法

                        
                    }
                    else {
                        //登录失败
                        System.out.println("您的密码错误，请确认");
                    }
                }

            } else{
                System.out.println("对不起，不存在该卡号的账户！");
            }
        }
    }

    private static void showUserCommand(Scanner sc,Account acc,ArrayList<Account> accounts) {
        while (true) {
        System.out.println("==================用户操作页面==================");
        System.out.println("1.查询账户");
        System.out.println("2.存款");
        System.out.println("3.取款");
        System.out.println("4.转账");
        System.out.println("5.修改密码");
        System.out.println("6.退出");
        System.out.println("7.注销账户");
        System.out.println("请您输入操作命令：");
        int command=sc.nextInt();
         switch (command)
            {
                case 1:
                    //查询账户
                    showAccount(acc);
                    break;
                case 2:
                    //存款
                    depositMoney(acc,sc);
                    break;
                case 3:
                    //取款
                    drawMoney(acc,sc);
                    break;
                case 4:
                    transferMoney(accounts,acc,sc);
                    //转账


                    break;
                case 5:
                    //修改密码
                    updatePassWord(acc,sc);
                    return;//结束当前的操作方法
                case 6:
                    //退出
                    System.out.println("欢迎下次光临！！");
                    return;//结束当前操作的方法
                case 7:
                    //注销账户
                    //从当前集合中抹掉当前账户对象即可
                    accounts.remove(acc);
                    System.out.println("销户成功了!!");
                    return;
                default://结束当前操作的方法！
                    System.out.println("您的命令输入有误");

            }
        }

    }

    private static void updatePassWord(Account acc,Scanner sc) {
        System.out.println("============修改密码==============");
        while (true) {
            System.out.println("请您输入正确的密码");
            String okPassWord=sc.next();
            //判断密码是否正确
            if(acc.getPassWord().equals(okPassWord)){
                //可以输入新密码
                while (true) {
                    System.out.println("请您输入新的密码：");
                    String newPassWord= sc.next();
                    System.out.println("请您输入确认密码：");
                    String oknewPassWord= sc.next();
                    if(newPassWord.equals(oknewPassWord))
                    {   //修改账户对象的密码为新密码
                        acc.setPassWord(newPassWord);
                        return;//直接结束！！
                    }
                    else{
                        System.out.println("您两次输入的密码不一致~~");
                    }
                }


            }
            else{
                System.out.println("当前输入的密码不正确~~~");

            }
        }
    }

    // 转账功能
    private static void transferMoney(ArrayList<Account> accounts, Account acc, Scanner sc) {
  //1.判断系统中是否有2个账户及以上
        if(accounts.size()<2)
        {
            System.out.println("对不起，系统中无其他账户，您不可以转账！");
            return;
        }
        //2.判断自己的账户对象是否有钱
        if(acc.getMoney()==0){
            System.out.println("对不起，您自己都没钱，就别转了~~~~");
            return;
        }
        //3.开始转账逻辑
        while (true) {
            System.out.println("请您输入对方账户的卡号：");
            String cardId=sc.next();
            Account  account = getAccountByCardId(cardId,accounts);
            //判断这个账户对象是否存在，存在说明对方卡号输入正确
            if(account!=null){
                //判断这个账户对象是否是当前登录的账户自己
                if(account.getCardId().equals(acc.getCardId())){
                    //正在给自己转账
                    System.out.println("您不可以为自己转账！");
                }
                else{
                    //确认对方的姓氏
                    String name="*"+account.getUserName().substring(1);
                    System.out.println("请您确认{"+name+"}的姓氏：");
                    String preName= sc.next();
                    //判断
                    if(account.getUserName().startsWith(preName)){
                        //真正开始转账
                        System.out.println("请你输入转账的金额：");
                        double money=sc.nextDouble();
                        //判断这个金额是否超过了自己的余额
                        if(money>acc.getMoney())
                        {
                            System.out.println("对不起，您要转账的金额太多，您最多可以转账多少："+acc.getMoney());
                        }
                        else {
                            //真的可以转了
                            acc.setMoney(acc.getMoney()-money);
                            account.setMoney(account.getMoney()+money);
                            System.out.println("恭喜您，转账成功了，已经为"+account.getUserName()+"转账多少："+money);
                            showAccount(acc);
                            return;
                        }
                    }
                    else{
                        System.out.println("对不起，您认证的信息有误~~~");
                    }

                }
            }
            else{
                System.out.println("对不起，您输入的转账卡号有问题！");
            }
        }
    }

    //是为了取款
    private static void drawMoney(Account acc, Scanner sc) {
        System.out.println("==============取款操作================");
        //1.判断它的账户是否足够100元
        if(acc.getMoney()>=100){
            while (true) {
                System.out.println("请你输入存款金额：");
                double money=sc.nextDouble();
                //2.判断这个金额有没有超过单次限额
                if(money>acc.getQuotaMoney()){
                    System.out.println("您单次取款金额超过每次限额，不要取那么多，每次最多可以取："+acc.getQuotaMoney());
                }
                else{
                    //3.判断当前余额是否足够你取钱
                    if(acc.getMoney()>=money){
                        //够钱，可以取钱
                        acc.setMoney(acc.getMoney()-money);
                        System.out.println("恭喜您，取钱"+money+"成功了！当前账户还剩余："+acc.getMoney());
                        return;//取钱后干掉取钱方法

                    }
                    else{
                        System.out.println("余额不足啊！");
                    }

                }
            }

        }
        else
        {
            System.out.println("您自己的金额没有超过100元，就别取了~~~~");
        }
    }

    /*
    存钱
     */
    private static void depositMoney(Account acc,Scanner sc) {
        System.out.println("===================存钱操作===================");
        System.out.println("请您输入存款的金额：");
        double money=sc.nextDouble();
        //直接把金额修改到账户对象的money属性中去
        acc.setMoney(acc.getMoney()+money);
        System.out.println("存款完成！！");
        showAccount(acc);
    }
//账户详细展示
    private static void showAccount(Account acc) {
        System.out.println("===================当前账户详情=========================");
        System.out.println("卡号："+acc.getCardId());
        System.out.println("姓名"+acc.getUserName());
        System.out.println("余额"+acc.getMoney());
        System.out.println("当次限额"+acc.getQuotaMoney());
    }

    //用户开户功能
    private static void register(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("==========用户开户功能==========");
        //2.键盘录入姓名 密码 确认密码
        System.out.println("请您输入开户名称；");
        String name = sc.next();
        String password = "";
        while (true) {
            System.out.println("请您输入开户密码：");
            password = sc.next();
            System.out.println("请您输入确认密码：");
            String okPassword = sc.next();
            //判断两次输入的密码是否一致
            if (okPassword.equals(password)) {
                break;
            } else {
                System.out.println("两次密码必须一致~~~");
            }
        }
        System.out.println("请您输入当次限额：");
        double quotaMoney= sc.nextDouble();
        //3.生成 一个账户的卡号，卡号是八位，而且不能与其他账户重复
          String cardId=createCardId(accounts);
        //4.创建一个账户对象封装账户信息
        //public Account(String cardId, String userName, String passWord, double money, double quotaMoney)
        Account account=new Account(cardId,name,password,quotaMoney);
        //5.把账户对象添加到集合中去
        accounts.add(account);
        System.out.println("恭喜您，您开户成功，您的卡号是："+account.getCardId()+"请你妥善保管");
    }

    public static String createCardId(ArrayList<Account> accounts) {
        while (true) {
            //生成八位随机的数字代表卡号
            String cardId = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                cardId += r.nextInt(10);
            }
            //判断卡号是否重复
            Account acc=getAccountByCardId(cardId,accounts);
            if(acc==null){
                //说明当前卡号没有重复
                return cardId;
            }
        }
    }
    public static Account getAccountByCardId(String cardId,ArrayList<Account> accounts){
      //根据卡号查询账户对象
        for (int i = 0; i < accounts.size(); i++) {
          Account acc=accounts.get(i);
          if(acc.getCardId().equals(cardId))
          {
              return acc;
          }
        }
        return null;//查无此账户，说明卡号没有重复！
    }
}
