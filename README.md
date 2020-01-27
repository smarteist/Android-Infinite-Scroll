# Android Infinite (Endless) Scroll

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/smarteist/Android-Infinite-Scroll) [![Build Status](https://img.shields.io/badge/license-Apache-blue)](http://www.apache.org/licenses/LICENSE-2.0)


This is a simple infinite scroll provider for recycler view, it can easily attach to RecyclerView and achieve this feature like a charm.

## Features

  - Ability to attach or detach from recycler on runtime.
  - Reverse mode for chat pages or etc.

You can also:
  - use it in nested scroll views.


AIS is lightweight class , actually it is a scroll listener extended from ```RecyclerView.OnScrollListener``` with a production ready implementation of the Infinite Scroll feature.

### Dependencies

* [AdnroidX](https://developer.android.com/jetpack/) - Android Jetpack is a suite of libraries
* [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview) - The RecyclerView widget is a more advanced and flexible version of ListView.

### Installation

Add this dependency in your gradle.

```groovy

```

### Development
First instaciate the ```InfiniteScroll``` .

```kotlin
        val infiniteScroll = InfiniteScroll()
```
Then you can ```attach``` a recycler view to it.


| Args | Description |
| ------ | ------ |
| RecyclerView | The recycler to be attached |
| ItemsPerPage | The number of items added to the recycler each time |
| ReverseScrolling (Optional) | If the scrolling is from bottom to top set it ```true``` |


```kotlin
        infiniteScroll.attach(recyclerView, 8)
```
And set a load more listener interface to the infinite scroll.
```kotlin
       infiniteScroll.setOnLoadMoreListener(object : InfiniteScroll.OnLoadMoreListener {
            override fun onLoadMore(step: Int) {
                // add more items here
            }
        })
```
#### Done!
Some other methods

| Method | Description |
| ------ | ------ |
| detach() | detaches current [.recyclerView] from this infinite scroll. |
| retryLoadMore() | manually calling [OnLoadMoreListener]. |
| setThreshold() | threshold Setting the infinite scroll loading it can't be smaller than 2 for logical reasons! |
| setHasNestedScroll() | If your recyclerView is inside a nested scroll set it ```true``` |


Want to contribute? Great!
Fork this and make positive changes!

### Todos

 - New features

License
----
**Free Software, Hell Yeah!**
Apache v2
