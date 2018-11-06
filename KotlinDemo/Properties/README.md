# Properties

------

## val & var ##

 - var -- mutable
 - val -- read-only

## syntax for declare a property ##

     var <propertyName> [: <PropertyType>] [= <property_initializer>]
        [<getter>]
        [<setter>]

 

 - Getter
 

> val isEmpty: Boolean
        get() = this.size == 0
        

 - Setter


----------


    var counter = 0 // Note: the initializer assigns the backing field directly
        set(value) {
            if (value >= 0) field = value
        }
    
> If you need to change the visibility of an accessor or to annotate it, but don't need to change the default implementation, you can define the accessor without defining its body:

    var setterVisibility: String = "abc"
        private set // the setter is private and has the default implementation

    var setterWithAnnotation: Any? = null
        @Inject set // annotate the setter with Inject
		