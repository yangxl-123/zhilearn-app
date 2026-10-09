package com.example.literacy.data

object SampleContent {

    val characters = listOf(
        "日", "月", "水", "火", "山",
        "石", "田", "土", "人", "口",
        "目", "耳", "手", "足", "心",
        "大", "小", "多", "少", "中",
        "上", "下", "左", "右", "天",
        "地", "云", "雨", "风", "雪",
        "花", "草", "树", "木", "鸟",
        "鱼", "牛", "羊", "马", "虫",
        "爸", "妈", "哥", "姐", "弟",
        "妹", "爷", "奶", "师", "友"
    )

    val articles = listOf(
        ArticleEntity(
            id = "a1",
            title = "日月山水",
            level = 1,
            content = "天上有日，天上有月。山上有水，山下有石。",
            question = "天上有日和什么？",
            answer = "月"
        ),
        ArticleEntity(
            id = "a2",
            title = "人口手足",
            level = 1,
            content = "人有口，人有手，人有足。口可以说话，手可以做事。",
            question = "手可以做什么？",
            answer = "做事"
        ),
        ArticleEntity(
            id = "a3",
            title = "大小多少",
            level = 2,
            content = "大山小石，多花少草。天上多云，地下多土。",
            question = "天上多什么？",
            answer = "云"
        ),
        ArticleEntity(
            id = "a4",
            title = "风雨雨雪",
            level = 2,
            content = "天上有风，天上有云。云中有雨，冬中有雪。",
            question = "冬中有什么？",
            answer = "雪"
        ),
        ArticleEntity(
            id = "a5",
            title = "花草树木",
            level = 2,
            content = "地上有花，地上有草。山上有树，林中有木。",
            question = "林中有什么？",
            answer = "木"
        ),
        ArticleEntity(
            id = "a6",
            title = "牛羊马虫",
            level = 3,
            content = "山上有牛，田中有羊。地上有马，草中有虫。",
            question = "田中有什么？",
            answer = "羊"
        ),
        ArticleEntity(
            id = "a7",
            title = "爸爸妈妈",
            level = 3,
            content = "我家有爸，我家有妈。爸爱我，妈爱我。",
            question = "谁爱我？",
            answer = "爸和妈"
        ),
        ArticleEntity(
            id = "a8",
            title = "哥哥姐姐",
            level = 3,
            content = "我家有哥，我家有姐。哥帮我，姐陪我。",
            question = "姐陪我吗？",
            answer = "是"
        ),
        ArticleEntity(
            id = "a9",
            title = "天地日月",
            level = 4,
            content = "天上有日，地下有土。山中有石，水中有鱼。",
            question = "水中有什么？",
            answer = "鱼"
        ),
        ArticleEntity(
            id = "a10",
            title = "师友同学",
            level = 4,
            content = "学校有师，学校有友。师教我，友陪我。",
            question = "友做什么？",
            answer = "陪我"
        )
    )
}
