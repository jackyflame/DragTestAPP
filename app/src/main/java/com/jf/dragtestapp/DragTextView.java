package com.jf.dragtestapp;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;

public class DragTextView extends android.support.v7.widget.AppCompatEditText {

    private String selectedStr = "";

    public DragTextView(Context context) {
        super(context);
        init();
    }

    public DragTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
//        setCustomSelectionActionModeCallback(new AbsListView.MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {}
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                //这里可以添加自己的菜单选项（前提是要返回true的）
//                try {
//                    Field mEditor = TextView.class.getDeclaredField("mEditor");//找到 TextView中的成员变量mEditor
//                    mEditor.setAccessible(true);
//                    Object object= mEditor.get(this);//根具持有对象拿到mEditor变量里的值 （android.widget.Editor类的实例）
//                    //--------------------显示选择控制工具------------------------------//
//                    Class mClass=Class.forName("android.widget.Editor");//拿到隐藏类Editor；
//                    Method method=mClass.getDeclaredMethod("getSelectionController");//取得方法  getSelectionController
//                    method.setAccessible(true);//取消访问私有方法的合法性检查
//                    Object resultobject=method.invoke(object);//调用方法，返回SelectionModifierCursorController类的实例
//
//                    Method show=resultobject.getClass().getDeclaredMethod("show");//查找 SelectionModifierCursorController类中的show方法
//                    show.invoke(resultobject);//执行SelectionModifierCursorController类的实例的show方法
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        setHasTransientState(true);
//                    }
//
//                    //--------------------忽略最后一次TouchUP事件-----------------------------------------------//
//                    Field  mSelectionActionMode=mClass.getDeclaredField("mDiscardNextActionUp");//查找变量Editor类中mDiscardNextActionUp
//                    mSelectionActionMode.setAccessible(true);
//                    mSelectionActionMode.set(object,true);//赋值为true
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                //返回false 就是屏蔽ActionMode菜单
//                return false;
//            }
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
//        });
//        setTextIsSelectable(true);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
        //不做任何处理，为了阻止长按的时候弹出上下文菜单
    }

    @Override
    protected boolean getDefaultEditable() {
        return false;
    }

    private int off; //字符串的偏移值
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Layout layout = getLayout();
        int line;
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                line = layout.getLineForVertical(getScrollY()+ (int)event.getY());
                off=layout.getOffsetForHorizontal(line,(int)event.getX());
                Selection.setSelection(getEditableText(),off);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                line=layout.getLineForVertical(getScrollY()+(int)event.getY());
                int curOff=layout.getOffsetForHorizontal(line,(int)event.getX());
                Selection.setSelection(getEditableText(),off,curOff);
                if(getEditableText() != null && getEditableText().length() > 0){
                    if(off<0 || curOff<0 || off>=getEditableText().length() || curOff>getEditableText().length() || off>curOff){
                        Log.e("DragTextView","selectedStr index error");
                        break;
                    }else{
                        if(off == curOff){
                            selectedStr = "";
                        }else {
                            selectedStr = getEditableText().subSequence(off,curOff).toString();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        setSelectHandleDisabled();
//                    }
//                }, 50); // 延迟50ms，等room显示handle完后，再隐藏
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
//
//    private void setSelectHandleDisabled() {
//        try {
//            Field mEditor = TextView.class.getDeclaredField("mEditor");
//            mEditor.setAccessible(true);
//            Object object = mEditor.get(this);
//            Class mClass = Class.forName("android.widget.Editor");
//            // 选中时handle
//            Method selectionController = mClass.getDeclaredMethod("getSelectionController");
//            selectionController.setAccessible(true);
//            Object invokeSelect = selectionController.invoke(object);
//            Method hideSelect = invokeSelect.getClass().getDeclaredMethod("hide");
//            hideSelect.invoke(invokeSelect);
//            // 插入时handle
//            Method insertionController = mClass.getDeclaredMethod("getInsertionController");
//            insertionController.setAccessible(true);
//            Object invokeInsert = insertionController.invoke(object);
//            Method hideInsert = invokeInsert.getClass().getDeclaredMethod("hide");
//            hideInsert.invoke(invokeInsert);
//        } catch (Exception e) {
//
//        }
//
//    }


    public String getSelectedStr() {
        return selectedStr;
    }
}
