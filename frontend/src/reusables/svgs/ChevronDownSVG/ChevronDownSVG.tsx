import React from 'react';

interface Props {
  svgStyle?: React.CSSProperties;
  pathStyle?: React.CSSProperties;
}

export const ChevronDownSVG: React.FC<Props> = ({ svgStyle, pathStyle }) => {
  return (
    <svg
      style={svgStyle}
      width='16'
      height='8'
      viewBox='0 0 16 8'
      fill='none'
      xmlns='http://www.w3.org/2000/svg'
    >
      <path
        style={pathStyle}
        d='M14.6924 0L16 1.1898L8.8734 7.67036C8.75921 7.77483 8.62342 7.85773 8.47384 7.9143C8.32426 7.97088 8.16386 8 8.00185 8C7.83984 8 7.67944 7.97088 7.52986 7.9143C7.38029 7.85773 7.24449 7.77483 7.1303 7.67036L0 1.1898L1.30763 0.00112152L8 6.08359L14.6924 0Z'
        fill='#A9A9B0'
      />
    </svg>
  );
};
