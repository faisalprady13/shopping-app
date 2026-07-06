export type Screen = 'start' | 'lists' | 'details'

export type ShoppingItem = {
  id: number;
  name: string;
  quantity: string;
  status: boolean;
};

export type ShoppingList = {
  id: number;
  name: string;
  date: string;
  products: ShoppingItem[];
};

export type UserDto = {
  name: string
}