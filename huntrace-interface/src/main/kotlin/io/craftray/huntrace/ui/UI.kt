package io.craftray.huntrace.ui

/**
 * @Author xbaimiao
 * @Date 2021/11/25 19:26
 */
interface UI {

    val sort: UISort

    val title: String

    val items: List<UIItem>
}