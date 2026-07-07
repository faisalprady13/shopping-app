export type Screen = 'start' | 'lists' | 'details' | 'add'

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

export type User = {
  id: string
};

export type UserDto = {
  name: string
};

export type ShoppingListDto = {
  name: string,
  user: User
};
