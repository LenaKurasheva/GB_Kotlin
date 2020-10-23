package com.lenakurasheva.notes.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FABBehavior(context: Context, attributeSet: AttributeSet)
    : FloatingActionButton.Behavior(context, attributeSet) {

    // Вызывается при начале скролла.
    // Здесь мы определяем направление скрола, и если оно вертикальное — перехватываем управление поведением:
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton,
                                     directTargetChild: View,
                                     target: View,
                                     axes: Int,
                                     type: Int): Boolean {

        return  axes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout,
                child, directTargetChild, target, axes, type)
    }

    // В onNestedScroll() — приходят координаты в процессе скрола, и мы можем управлять поведением кнопки,
    // которая приходит в метод параметром child.
    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: FloatingActionButton,
                                target: View,
                                dxConsumed: Int,
                                dyConsumed: Int,
                                dxUnconsumed: Int,
                                dyUnconsumed: Int,
                                type: Int,
                                consumed: IntArray) {

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)

        // Проверяем: если скролл направлен вверх, вызываем метод hide() у FloatingActionButton, который скрывает кнопку с анимацией:
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.hide(object : FloatingActionButton.OnVisibilityChangedListener() {

                @SuppressLint("RestrictedApi")
                override fun onHidden(fab: FloatingActionButton) {
                    fab.visibility = View.INVISIBLE
                }
            })
        // Если вниз, вызываем метод show(), который показывает ее с анимацией:
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
        }
    }
}