import { Links } from '../enums/Links';
import { NavItems } from '../enums/NavItems';
import { store } from '../store/store';

const jwtToken = store.getState().jwtToken;

export const APPEARING_DP_CSS_PROPS: React.CSSProperties = {
  opacity: 1,
  transform: 'scaleY(1)',
};

export const HEADER_NAV_ITEMS = Object.values(NavItems).filter(value => {
  return !!jwtToken || value !== NavItems.calendar;
}); // we need NavItems.calendar for sidebar, but not for nav

export const HEADER_NAV_LINKS = Object.values(Links).filter(link => {
  const value = link.slice(1); // cut slash

  if (
    Object.keys(NavItems).includes(value) &&
    (!!jwtToken || link !== Links.calendar)
  ) {
    return link;
  }
});

export const EVENTS_GAP = '40px';
