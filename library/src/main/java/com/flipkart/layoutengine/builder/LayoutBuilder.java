package com.flipkart.layoutengine.builder;

import android.view.View;

import com.flipkart.layoutengine.parser.LayoutHandler;
import com.flipkart.layoutengine.toolbox.BitmapLoader;
import com.flipkart.layoutengine.toolbox.Styles;
import com.flipkart.layoutengine.view.ProteusView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author kirankumar
 * @author aditya.sharat
 */
public interface LayoutBuilder {

    /**
     * Register {@link LayoutHandler}s for custom view types.
     *
     * @param type    The name of the view type.
     * @param handler The {@link LayoutHandler} to use while building this view type.
     */
    void registerHandler(String type, LayoutHandler handler);

    /**
     * Un-Register the {@link LayoutHandler} registered using {@link LayoutBuilder#registerHandler}.
     *
     * @param type remove {@link LayoutHandler} for the specified view type.
     */
    void unregisterHandler(String type);

    /**
     * Returns the {@link LayoutHandler} for the specified view type.
     *
     * @param type The name of the view type.
     * @return The {@link LayoutHandler} associated to the specified view type
     */
    LayoutHandler getHandler(String type);

    /**
     * This method is used to process the attributes from the layout and set them on the {@link View}
     * that is being built.
     *
     * @param handler
     * @param view    The view to handle the attribute on.
     * @return true if the attribute is processed false otherwise.
     */
    boolean handleAttribute(LayoutHandler handler, ProteusView view, String attribute, JsonElement value);

    /**
     * This methods builds a {@link ProteusView} from a layout {@link JsonObject} and data {@link JsonObject}.
     *
     * @param parent The intended parent view for the {@link View} that will be built.
     * @param layout The {@link JsonObject} which defines the layout for the {@link View} to be built.
     * @param data   The {@link JsonObject} which will be used to replace bindings with values in the {@link View}.
     * @param index  The index of this view in its parent. Pass 0 if it has no parent.
     * @param styles The styles to be applied to the view.
     * @return A {@link ProteusView} with the built view, an array of its children and optionally its bindings.
     */
    ProteusView build(View parent, JsonObject layout, JsonObject data, int index, Styles styles);

    /**
     * @return The callback object used by this {@link LayoutBuilder}
     */
    LayoutBuilderCallback getListener();

    /**
     * Used to set a callback object to handle unknown view types and unknown attributes and other
     * exceptions. This callback is also used for requesting {@link android.support.v4.view.PagerAdapter}s
     * and {@link android.widget.Adapter}s
     *
     * @param listener The callback object.
     */
    void setListener(LayoutBuilderCallback listener);

    /**
     * @return The helper object that is being used to handle drawables that need to fetched from a
     * network.
     */
    BitmapLoader getNetworkDrawableHelper();

    /**
     * All network bitmap calls will be handed over to this loader. This method is used to
     * set the {@link com.flipkart.layoutengine.toolbox.BitmapLoader} for the
     * {@link com.flipkart.layoutengine.builder.LayoutBuilder}
     *
     * @param bitmapLoader {@link com.flipkart.layoutengine.toolbox.BitmapLoader} to use for
     *                     loading images.
     */
    void setBitmapLoader(BitmapLoader bitmapLoader);

    /**
     * @return true when rendering preview immediately by this {@link LayoutBuilder} synchronously
     * otherwise false.
     */
    boolean isSynchronousRendering();

    /**
     * Set this to true for rendering preview immediately. This is to be used to decide whether
     * remote resources like remote images are to be downloaded synchronously or not
     */
    void setSynchronousRendering(boolean isSynchronousRendering);
}
