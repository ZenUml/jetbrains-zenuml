package org.intellij.sequencer.diagram;

import com.intellij.ui.JBColor;
import org.apache.log4j.Logger;
import org.intellij.sequencer.config.Configuration2;

import java.awt.*;

public class DisplayMethod extends ScreenObject {

    private static final Logger LOGGER = Logger.getLogger(DisplayMethod.class);

    private static final Paint LINE_COLOR = JBColor.foreground();
    private static final Paint SHADOW_COLOR = JBColor.LIGHT_GRAY;

    private ObjectInfo _objectInfo;
    private MethodInfo _methodInfo;
    private DisplayLink _call;
    private DisplayLink _callReturn;
    private int _horizontalSeq;

    DisplayMethod(ObjectInfo objectInfo, MethodInfo methodInfo,
                  DisplayLink call, DisplayLink callReturn) {
        _objectInfo = objectInfo;
        _methodInfo = methodInfo;
        _call = call;
        _callReturn = callReturn;
    }

    void setHorizontalSeq(int horizontalSeq) {
        if(LOGGER.isDebugEnabled())
            LOGGER.debug("DisplayMethod setHorizontalSeq(" + horizontalSeq + ")");
        _horizontalSeq = horizontalSeq;
    }

    public MethodInfo getMethodInfo() {
        return _methodInfo;
    }

    int getStartSeq() {
        return _call.getSeq();
    }

    int getEndSeq() {
        return _callReturn.getSeq();
    }

    public ObjectInfo getObjectInfo() {
        return _objectInfo;
    }

    public String getToolTip() {
        return _methodInfo.getHtmlDescription();
    }

    public void paint(Graphics2D g2) {
        Configuration2 configuration = Configuration2.getInstance();
        if(configuration.USE_3D_VIEW) {
            g2.setPaint(SHADOW_COLOR);
            g2.fillRect(getX() + 2, getY() + 2, getWidth(), getHeight());
        }
        g2.setPaint(isSelected() ? configuration.SELECTED_METHOD_BAR_COLOR : configuration.METHOD_BAR_COLOR);
        g2.fillRect(getX(), getY(), getWidth(), getHeight());
        g2.setPaint(LINE_COLOR);
        g2.drawRect(getX(), getY(), getWidth() - 1, getHeight() - 1);
    }

    public int getHeight() {
        return _callReturn.getY() + _callReturn.getTextHeight() - getY();
    }

    public int getWidth() {
        return 9;
    }

    public int getY() {
        return _call.getY() + _call.getTextHeight();
    }

    public int getX() {
        return _call.getTo().getCenterX() - 4 + (3 * _horizontalSeq);
    }

    public String toString() {
        return "DisplayMethod call <" + _call + "> return <" + _callReturn + ">";
    }
}
