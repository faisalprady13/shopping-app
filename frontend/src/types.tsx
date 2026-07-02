export type Screen = 'start' | 'lists' | 'details'

export type ShoppingItem = {
  id: number
  name: string
  quantity: string
  completed: boolean
}

export type ShoppingList = {
  id: number
  title: string
  createdAt: string
  items: ShoppingItem[]
}
