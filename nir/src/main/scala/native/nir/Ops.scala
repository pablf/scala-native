package native
package nir

import native.util.unreachable

sealed abstract class Op {
  final def resty: Type = this match {
    case Op.Call(Type.Function(_, ret), _, _) => ret
    case Op.Call(_, _, _)                     => unreachable
    case Op.Load(ty, _)                       => ty
    case Op.Store(_, _, _)                    => Intr.unit
    case Op.Elem(ty, _, _)                    => Type.Ptr(ty) // todo: ty @ index
    case Op.Extract(ty, _, _)                 => ??? // todo: ty @ index
    case Op.Insert(ty, _, _, _)               => ty
    case Op.Alloca(ty)                        => Type.Ptr(ty)
    case Op.Bin(_, ty, _, _)                  => ty
    case Op.Comp(_, _, _, _)                  => Type.Bool
    case Op.Conv(_, ty, _)                    => ty

    case Op.Alloc(ty)        => ty
    case Op.Field(ty, _, _)  => Type.Ptr(ty)
    case Op.Method(ty, _, _) => Type.Ptr(ty)
    case Op.Module(n)        => Type.ModuleClass(n)
    case Op.As(ty, _)        => ty
    case Op.Is(_, _)         => Type.Bool
    case Op.Copy(v)          => v.ty
    case Op.SizeOf(_)        => Type.Size
    case Op.TypeOf(_)        => Intr.type_
    case Op.StringOf(_)      => Intr.string
  }
}
object Op {
  // low-level
  final case class Call   (ty: Type, ptr: Val, args: Seq[Val])          extends Op
  final case class Load   (ty: Type, ptr: Val)                          extends Op
  final case class Store  (ty: Type, ptr: Val, value: Val)              extends Op
  // TODO: ty should be a pointee type, not result elem type
  final case class Elem   (ty: Type, ptr: Val, indexes: Seq[Val])       extends Op
  final case class Extract(ty: Type, aggr: Val, index: Val)             extends Op
  final case class Insert (ty: Type, aggr: Val, value: Val, index: Val) extends Op
  final case class Alloca (ty: Type)                                    extends Op
  final case class Bin    (bin: nir.Bin, ty: Type, l: Val, r: Val)      extends Op
  final case class Comp   (comp: nir.Comp, ty: Type, l: Val, r: Val)    extends Op
  final case class Conv   (conv: nir.Conv, ty: Type, value: Val)        extends Op

  // high-level
  final case class Alloc    (ty: Type)                         extends Op
  final case class Field    (ty: Type, obj: Val, name: Global) extends Op
  final case class Method   (ty: Type, obj: Val, name: Global) extends Op
  final case class Module   (name: Global)                     extends Op
  final case class As       (ty: Type, obj: Val)               extends Op
  final case class Is       (ty: Type, obj: Val)               extends Op
  final case class Copy     (value: Val)                       extends Op
  final case class SizeOf   (ty: Type)                         extends Op
  final case class TypeOf   (ty: Type)                         extends Op
  final case class StringOf (value: String)                    extends Op
}
