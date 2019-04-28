package com.fgh.alg.stack;

/**
 * 使用栈模拟浏览器的前进  后退
 *
 * @author fgh
 * @since 2019/4/12 16:40
 */
public class SampleBrowser {

    private String currentPage;
    private LinkedListBasedStack backStack;
    private LinkedListBasedStack forwardStack;

    public SampleBrowser() {
        this.backStack = new LinkedListBasedStack();
        this.forwardStack = new LinkedListBasedStack();
    }

    public void open(String url) {
        if (this.currentPage != null) {
            this.backStack.push(this.currentPage);
            this.forwardStack.clear();
        }
        showUrl(url, "open");
    }

    public boolean canGoBack() {
        return this.backStack.size() > 0;
    }

    public boolean canGoForward() {
        return this.forwardStack.size() > 0;
    }

    public String goBack() {
        if (this.canGoBack()) {
            this.forwardStack.push(this.currentPage);
            String backUrl = this.backStack.pop();
            showUrl(backUrl, "back");
            return backUrl;
        }
        System.out.println("cannot go back,no pages behind");
        return null;
    }

    public String goForward() {
        if (this.canGoForward()) {
            this.backStack.push(this.currentPage);
            String forwardUrl = this.forwardStack.pop();
            showUrl(forwardUrl, "forward");
            return forwardUrl;
        }
        System.out.println("cannot go forward,no pages ahead");
        return null;
    }

    public void showUrl(String url, String prefix) {
        this.currentPage = url;
        System.out.println(prefix + " page == " + url);
    }

    public String getCurrentPage() {
        return this.currentPage;
    }

    public static void main(String[] args) {
        SampleBrowser sampleBrowser = new SampleBrowser();
        sampleBrowser.open("https://www.badu.com");
        sampleBrowser.open("https://www.sina.com");
        sampleBrowser.open("https://www.jd.com");
        sampleBrowser.goBack();
        sampleBrowser.goBack();

        sampleBrowser.goForward();
        sampleBrowser.goForward();
        sampleBrowser.open("https://www.jd66.com");
        sampleBrowser.open("https://www.jd77..com");


        String currentPage = sampleBrowser.getCurrentPage();
        System.out.println("currentPage = " + currentPage);

    }
}
