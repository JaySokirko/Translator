package com.jay.translator;

public class EditTextLineCountChangeListener  {

    private int lineCount = 0;
    private ChangeListener listener;

    public int getLineCount() {
        return lineCount;
    }


    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
        if (listener != null) listener.onChange(lineCount);
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange(int lineCount);
    }
}
