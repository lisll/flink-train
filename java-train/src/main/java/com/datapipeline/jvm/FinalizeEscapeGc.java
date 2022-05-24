package com.datapipeline.jvm;

public class FinalizeEscapeGc {
    public static FinalizeEscapeGc SAVE_HOOK= null;
    public void isActive(){
        System.out.println("yes,i am stillavlie:");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed");
        FinalizeEscapeGc.SAVE_HOOK = this;
    }

    public static void main(String[] args) throws Exception {
        SAVE_HOOK = new FinalizeEscapeGc();
        // 对象第一次拯救自己
        SAVE_HOOK =null;
        System.gc();
        // 因为Finalize方法优先级低，所以暂停0.5秒
        Thread.sleep(500);
        if(SAVE_HOOK != null) SAVE_HOOK.isActive();
        else System.out.println("no i am dead:C");

        SAVE_HOOK = null;
        System.gc();
        // 即使这里暂停了一段时间，finalize方法也不会执行，因为这个方法只会执行一次
        Thread.sleep(900);
        if(SAVE_HOOK != null) SAVE_HOOK.isActive();
        else System.out.println("no i am dead:C");
    }
}
