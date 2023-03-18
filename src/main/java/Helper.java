public class Helper {
    public static void execute(String[] args) {
        if (args == null) {
            System.out.println("入参为必输项");
            return;
        }

        if ("1".equals(args[0])) {
            if (args.length != 4) {
                System.out.println("入参不正确");
                return;
            }

            new Dbf().handler(args[1], args[2], args[3]);
        }
    }

}
