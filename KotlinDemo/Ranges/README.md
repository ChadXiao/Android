# Ranges


----------


## .. ##
    
    

> 小于等于最大，大于等于最小

    
    if (i in 1..10) { // equivalent of 1 <= i && i <= 10
        println(i)
    }
    
## setp ##

    

> 步进

    for (i in 1..4 step 2) print(i)
    
## downTo ##

> 反序

    for (i in 4 downTo 1) print(i)
    
    for (i in 4 downTo 1 step 2) print(i)
    
## unit ##

    

> 小于最大，大于等于最小

    for (i in 1 until 10) {
        // i in [1, 10), 10 is excluded
        println(i)
    }
    
## 属性 ##

 - first    第一个数
 - last     最后一个数
 - step     步进
 
    

> (1..12 step 2).last == 11  // progression with values [1, 3, 5, 7, 9, 11]
>     (1..12 step 3).last == 10  // progression with values [1, 4, 7, 10]
>     (1..12 step 4).last == 9