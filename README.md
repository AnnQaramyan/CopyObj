Sometimes you want to make a complete copy of some object.

Something like this:

ComplexObject obj = ...
ComplexObject copy = CopyUtils.deepCopy(obj);
The problem is that classes in Java can be of arbitrary complexity - the number of class fields and their types are not regulated in any way. Moreover, the type system in Java is closed - elements of an array/list can be absolutely any data types, including arrays and lists. And also there are recursive data structures - when an object somewhere in its depths contains a reference to itself (or to a part of itself).

You need to write a deepCopy() method that takes all these nuances into account and works on objects of arbitrary structure and size.
