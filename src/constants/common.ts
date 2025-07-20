import { Links } from '../enums/Links';
import { NavItems } from '../enums/NavItems';

export const APPEARING_DP_CSS_PROPS: React.CSSProperties = {
  opacity: 1,
  transform: 'scaleY(1)',
};

export const HEADER_NAV_ITEMS = Object.values(NavItems).filter(
  value => value !== NavItems.calendar,
); // we need NavItems.calendar for sidebar, but not for nav

export const HEADER_NAV_LINKS = Object.values(Links).filter(value => {
  const slicedValue = value.slice(1); // cut slash

  if (Object.keys(NavItems).includes(slicedValue) && value !== Links.calendar) {
    return value;
  }
});
