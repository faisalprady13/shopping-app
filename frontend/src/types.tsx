export type Screen = 'start' | 'lists' | 'details'

export type ShoppingItem = {
  id: string;
  name: string;
  quantity: string;
  status: boolean;
};

export type ShoppingList = {
  id: string;
  name: string;
  date: string;
  products: ShoppingItem[];
};

export type UserDto = {
  name: string
}