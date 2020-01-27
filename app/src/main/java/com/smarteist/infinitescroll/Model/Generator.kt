package com.smarteist.infinitescroll.Model

import com.smarteist.infinitescroll.R

class Generator {

    companion object PostGenerator{

        val images = listOf(
            R.drawable.screenshot1,
            R.drawable.screenshot2,
            R.drawable.screenshot3,
            R.drawable.screenshot4,
            R.drawable.screenshot5,
            R.drawable.screenshot6,
            R.drawable.screenshot7,
            R.drawable.screenshot8
        )

        fun getPosts(count: Int): MutableList<Post> {
            val posts: MutableList<Post> = mutableListOf()

            for (i in 1..count) {
                posts.add(
                    Post(
                        "Hello, it is a title",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Et odio pellentesque diam volutpat commodo sed egestas egestas. Sociis natoque penatibus et magnis dis. Nunc sed blandit libero volutpat sed. At erat pellentesque adipiscing commodo elit. Nibh sit amet commodo nulla. Semper auctor neque vitae tempus quam pellentesque. Pharetra et ultrices neque ornare aenean euismod elementum nisi. Felis imperdiet proin fermentum leo vel orci. Lacinia at quis risus sed. Quis varius quam quisque id diam vel quam elementum pulvinar. Urna nec tincidunt praesent semper feugiat nibh sed pulvinar. Porttitor eget dolor morbi non. Ut ornare lectus sit amet est placerat in. Ipsum suspendisse ultrices gravida dictum fusce ut placerat. Elit eget gravida cum sociis. Ultricies lacus sed turpis tincidunt id. Posuere lorem ipsum dolor sit amet. Urna molestie at elementum eu facilisis. Sed vulputate odio ut enim blandit volutpat."
                        , images.shuffled().get(0)
                    )
                )
            }
            return posts
        }
    }
}